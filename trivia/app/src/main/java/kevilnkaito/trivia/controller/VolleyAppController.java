package kevilnkaito.trivia.controller;

import android.app.Application;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleyAppController extends Application {

    private static VolleyAppController instance;
    private RequestQueue requestQueue;
    //private static Context ctx;

    //Vì [extends Application] nên không cần cái này:
    /*private AppController(Context context) {

        ctx = context;
        requestQueue = getRequestQueue();

    }*/

    public static synchronized VolleyAppController getInstance() {

        //Vì [extends Application] nên không cần cái này:
        /*if (instance == null) {
            instance = new AppController(context);
        }*/

        return instance;

    }

    public RequestQueue getRequestQueue() {

        if (requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return requestQueue;

    }

    public <T> void addToRequestQueue(Request<T> request) {

        getRequestQueue().add(request);

    }

    //Vì [extends Application] nên THÊM cái này:
    @Override
    public void onCreate() {

        super.onCreate();

        instance = this;

    }

}
