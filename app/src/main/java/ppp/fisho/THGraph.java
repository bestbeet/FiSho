package ppp.fisho;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

/**
 * Created by best on 22/8/2560.
 */

public class THGraph extends Fragment {

    WebView webView;
    SwipeRefreshLayout swipeRefreshLayout;
    String html = "<iframe width=\"450\" height=\"260\" style=\"border: 1px solid #cccccc;\" src=\"https://thingspeak.com/apps/matlab_visualizations/168600\"></iframe>";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.thgraph_layout, container , false);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.graph);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getActivity().setTitle("Statistics");
        //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //imageView = (ImageView) view.findViewById(R.id.imageStat);
        //Picasso.with(getActivity()).load(url).into(imageView);
        webView = (WebView) view.findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadData(html, "text/html", null);


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                //Picasso.with(getActivity()).load(url).into(imageView);
                webView.loadData(html, "text/html", null);
            }
        });
        return view;
    }
}