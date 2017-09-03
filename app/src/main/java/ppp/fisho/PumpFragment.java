package ppp.fisho;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by best on 29/3/2560.
 */

public class PumpFragment extends Fragment {
    @Nullable

    private Button ledOn, ledOff;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.pump_layout,container, false);
        //ipAddress = (EditText) view.findViewById(R.id.edt_ip);
        ledOn = (Button) view.findViewById(R.id.btn_ledOn);
        ledOff = (Button) view.findViewById(R.id.btn_ledOff);
        ledOn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                String serverAdress = "192.168.137.45" + ":" + "80";
                HttpRequestTask requestTask = new HttpRequestTask(serverAdress);
                requestTask.execute("1");
                Toast.makeText(PumpFragment.this.getActivity(), "ON", Toast.LENGTH_SHORT).show();
            }
        });
        ledOff.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                String serverAdress = "192.168.137.45" + ":" + "80";
                HttpRequestTask requestTask = new HttpRequestTask(serverAdress);
                requestTask.execute("1");
                Toast.makeText(PumpFragment.this.getActivity(), "OFF", Toast.LENGTH_SHORT).show();
            }
        });
        return  view;
    }

    private class HttpRequestTask extends AsyncTask<String, Void, String> {

        private String serverAdress;
        private String serverResponse = "";

        public HttpRequestTask(String serverAdress) {
            this.serverAdress = serverAdress;
        }

        @Override
        protected String doInBackground(String... params) {


            String val = params[0];
            final String url = "http://" + serverAdress + "/led/" + val;

            try {
                HttpClient client = new DefaultHttpClient();
                HttpGet getRequest = new HttpGet();
                getRequest.setURI(new URI(url));
                HttpResponse response = client.execute(getRequest);

                InputStream inputStream = null;
                inputStream = response.getEntity().getContent();
                BufferedReader bufferedReader =
                        new BufferedReader(new InputStreamReader(inputStream));

                serverResponse = bufferedReader.readLine();
                inputStream.close();

            } catch (URISyntaxException e) {
                e.printStackTrace();
                serverResponse = e.getMessage();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
                serverResponse = e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                serverResponse = e.getMessage();
            }

            return serverResponse;
        }

        @Override
        protected void onPostExecute(String s) {
        }

        @Override
        protected void onPreExecute() {
        }
    }

}
