package singwaichan.android.usatodayhttpsample;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    private TextView resultTextView;
    private ArrayList<NewsArticleHolder.ArticleItem> articleItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultTextView = (TextView) this.findViewById(R.id.result);
        USANewsAsyncTask getNewsUpdate = new USANewsAsyncTask();
        getNewsUpdate.execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class USANewsAsyncTask extends AsyncTask<Void, Void, ArrayList<NewsArticleHolder.ArticleItem>> {

        private String APILink = "http://api.usatoday.com/open/articles/topnews/";
        private String API_KEY = "p94pbb9k8d5c9yn9aj6dmaqb";
        private String[] SECTION = {"tech", "news", "sport", "money"};

        @Override
        protected ArrayList<NewsArticleHolder.ArticleItem> doInBackground(Void... params) {
            String result = "";
            //For some reason post method doesn't work.
            //Only Get request work for this API.
            //Prepare Post request.
            HttpClient httpClient = new DefaultHttpClient();

            //Add all array list
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("count", "10"));
            nameValuePairs.add(new BasicNameValuePair("encoding", "json"));
            nameValuePairs.add(new BasicNameValuePair("api_key", API_KEY));
            String paramsString = URLEncodedUtils.format(nameValuePairs, "UTF-8");

            Log.e("Get link result ", APILink + SECTION[0] + "?" + paramsString);
            //Build Link
            HttpGet httpget = new HttpGet(APILink + SECTION[0] + "?" + paramsString);
            //Execute and get the response.
            HttpResponse response = null;
            try {
                response = httpClient.execute(httpget);
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream inputStream = null;
                try {
                    inputStream = entity.getContent();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                try {
                    while ((line = bufferedReader.readLine()) != null) {
                        result += line;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Log.e("", "result" + result);

            //String to Json
            JSONObject jsonObject = null;
            if (JSONValue.isValidJson(result)) {
                jsonObject = (JSONObject) JSONValue.parse(result);
            }
            ;

            JSONArray stories = (JSONArray) jsonObject.get("stories");
            //Log.e ("all info" , "STORY ARRAY" + stories.toString());
            String[] JSONKey = MainActivity.this.getResources().getStringArray(R.array.USATodayJSONKey);
            ArrayList<NewsArticleHolder.ArticleItem> articleItems = new ArrayList<>();
            for (int i = 0; i < stories.size(); i++) {
                JSONObject entry = (JSONObject) stories.get(i);
                NewsArticleHolder.ArticleItem temp = new NewsArticleHolder.ArticleItem(
                        (String) entry.get(JSONKey[1]),
                        (String) entry.get(JSONKey[2]),
                        (String) entry.get(JSONKey[3]),
                        (String) entry.get(JSONKey[4])
                );
                articleItems.add(temp);
            }


            return articleItems;
        }

        @Override
        protected void onPostExecute(ArrayList<NewsArticleHolder.ArticleItem> result) {
            MainActivity.this.articleItems = result;
            //Update First UI and send result back
        }

    }
}

