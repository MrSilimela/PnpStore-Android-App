package com.pnp.store.online.Fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;
import com.amigold.fundapter.interfaces.DynamicImageLoader;
import com.amigold.fundapter.interfaces.ItemClickListener;
import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pnp.store.online.Interfaces.Cart;
import com.pnp.store.online.Interfaces.UILConfig;
import com.pnp.store.online.R;

import java.util.ArrayList;
import java.util.HashMap;

public class CartFragment extends Fragment implements AdapterView.OnItemClickListener, AsyncResponse{

    public static final String PREFS = "prefFile";

    final String LOG = "CartFragment";
    final static String url = "https://pnponlineshopping1.000webhostapp.com/FetchCart.php";
    int counter =1;


    View view;

    private ArrayList<Cart> itemList;
    private ListView lv;
    FunDapter<Cart> adapter;

    Button btnCheckout, btnReturn;



    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_cart, container, false);

        ImageLoader.getInstance().init(UILConfig.config(CartFragment.this.getActivity()));

        btnCheckout = (Button)view.findViewById(R.id.btnCheckout);
        btnReturn = (Button)view.findViewById(R.id.btnReturn);

        SharedPreferences preferences = CartFragment.this.getActivity().getSharedPreferences(PREFS, 0);
        String customer = preferences.getString("username", null);

        HashMap postData = new HashMap();
        postData.put("txtUsername", customer);
        PostResponseAsyncTask taskRead = new PostResponseAsyncTask(CartFragment.this.getActivity(), postData, this);
        taskRead.execute(url);

        return view;
    }

    @Override
    public void processFinish(String s)
    {
        itemList = new JsonConverter<Cart>().toArrayList(s, Cart.class);

        BindDictionary dic = new BindDictionary();

        dic.addStringField(R.id.tvName, new StringExtractor<Cart>() {
            @Override
            public String getStringValue(Cart item, int position) {
                return item.orderName;
            }
        });

        dic.addStringField(R.id.tvDesc, new StringExtractor<Cart>() {
            @Override
            public String getStringValue(Cart item, int position) {
                return item.description;
            }
        }).visibilityIfNull(View.GONE);

        dic.addStringField(R.id.edQty, new StringExtractor<Cart>() {
            @Override
            public String getStringValue(Cart item, int position) {
                return ""+ item.quantity;
            }
        });

        dic.addStringField(R.id.tvPrice, new StringExtractor<Cart>() {
            @Override
            public String getStringValue(Cart item, int position) {
                double price = item.price;
                int qty = item.quantity;
                price = price * qty;
                String s = String.format("%.2f",price);
                return  s;
            }
        });


        dic.addDynamicImageField(R.id.ivImage, new StringExtractor<Cart>() {
            @Override
            public String getStringValue(Cart item, int position) {
                return item.img_url;
            }
        }, new DynamicImageLoader() {
            @Override
            public void loadImage(String url, ImageView img) {
                //Set image
                ImageLoader.getInstance().displayImage(url, img);
            }
        });

        //Remove Item from the basket
        dic.addBaseField(R.id.btnRemove).onClick(new ItemClickListener() {

            @Override
            public void onClick(Object item, int position, View view) {

                final Cart selectedItem = itemList.get(position);
                HashMap postData = new HashMap();
                postData.put("pid", ""+ selectedItem.id);

                PostResponseAsyncTask taskRemove = new PostResponseAsyncTask(CartFragment.this.getActivity(),
                        postData, new AsyncResponse() {
                    @Override
                    public void processFinish(String s) {
                        Log.d(LOG, s);
                        if(s.contains("success"))
                        {
                            itemList.remove(selectedItem);
                            lv.refreshDrawableState();

                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.detach(CartFragment.this).attach(CartFragment.this).commit();

                            adapter.notifyDataSetChanged();
                            Toast.makeText(CartFragment.this.getActivity(), "Item Removed", Toast.LENGTH_SHORT).show();
                            counter = itemList.size();
                        }
                    }
                });
                taskRemove.execute("https://pnponlineshopping1.000webhostapp.com/DeleteCart.php");
            }
        });

        //increase the item quantity
        dic.addBaseField(R.id.qty_increase).onClick(new ItemClickListener() {
            @Override
            public void onClick(Object item, int position, final View view) {
                final Cart selectedItem = itemList.get(position);
                HashMap postData = new HashMap();
                postData.put("txtPid", ""+ selectedItem.id);
                postData.put("mobile", "android");

                final PostResponseAsyncTask incTask = new PostResponseAsyncTask(CartFragment.this.getActivity(),
                        postData, new AsyncResponse() {
                    @Override
                    public void processFinish(String s) {

                        if(s.contains("success"))
                        {
                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.detach(CartFragment.this).attach(CartFragment.this).commit();

                            adapter.notifyDataSetChanged();
                        }
                    }

                });
                incTask.execute("https://pnponlineshopping1.000webhostapp.com/incrQuantity.php");
            }
        });

        //Decrease the item quantity
        dic.addBaseField(R.id.qty_decrease).onClick(new ItemClickListener() {
            @Override
            public void onClick(Object item, int position, final View view) {
                final Cart selectedItem = itemList.get(position);
                HashMap postData = new HashMap();
                int qty = selectedItem.quantity;

                postData.put("txtPid", ""+ selectedItem.id);
                postData.put("mobile", "android");

                if(qty > 1)
                {
                    final PostResponseAsyncTask incTask = new PostResponseAsyncTask(CartFragment.this.getActivity(),
                            postData, new AsyncResponse() {
                        @Override
                        public void processFinish(String s) {

                            if(s.contains("success"))
                            {
                                FragmentTransaction ft = getFragmentManager().beginTransaction();
                                ft.detach(CartFragment.this).attach(CartFragment.this).commit();

                                adapter.notifyDataSetChanged();
                            }
                        }

                    });
                    incTask.execute("https://pnponlineshopping1.000webhostapp.com/descQuantity.php");
                }
            }
        });

        adapter = new FunDapter<>(CartFragment.this.getActivity(), itemList, R.layout.cart_row, dic);



            lv = (ListView) view.findViewById(R.id.lvCart);
            lv.setAdapter(adapter);

            lv.setOnItemClickListener(this);



        boolean retval = adapter.isEmpty();
            //check if cart is empty
        if (counter == 0) {


            TextView total = (TextView)view.findViewById(R.id.tvTotal);
            TextView empty = (TextView)view.findViewById(R.id.emptyElement);
            TextView EmptySpace = (TextView)view.findViewById(R.id.EmptySpace);
            ListView listView = (ListView)view.findViewById(R.id.lvCart);

            btnCheckout.setVisibility(View.GONE);
            total.setVisibility(View.GONE);
            listView.setVisibility(View.GONE);
            EmptySpace.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
        }


        double v = totalPrice(lv);

            btnCheckout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PaymentFragment paymentFragment = new PaymentFragment();
                    FragmentManager manager = getFragmentManager();
                    manager.beginTransaction().replace(R.id.content_main, paymentFragment)
                            .addToBackStack("Payment").commit();
                }
            });

            btnReturn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FreshFoodFragment FreshFoodFragment = new FreshFoodFragment();
                    FragmentManager manager = getFragmentManager();
                    manager.beginTransaction().replace(R.id.content_main, FreshFoodFragment)
                            .addToBackStack("Fresh Food").commit();
                }
            });





    }
    private double totalPrice(ListView listView)
    {
        double sum = 0;
        TextView total = (TextView)view.findViewById(R.id.tvTotal);
        int count = listView.getCount();
        for(int i = 0; i < count; i++)
        {
            View v = listView.getAdapter().getView(i, null, null);
            TextView tv = (TextView) v.findViewById(R.id.tvPrice);
            sum = sum + Double.parseDouble(tv.getText().toString());
        }

        String s = String.format("%.2f",sum);
        //String s = "" + sum;
        total.setText("Your Total: " +s);
        return sum;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
    }



}
