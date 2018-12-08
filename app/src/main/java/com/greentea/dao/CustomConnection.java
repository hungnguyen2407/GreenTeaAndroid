package com.greentea.dao;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.greentea.R;
import com.greentea.model.MemoryName;
import com.greentea.model.NameOfResources;

import org.json.JSONObject;

/**
 * This class is used to make connection to webservice
 * There are 4 kind of connection: GET, POST, PUT and DELETE
 * Choose the type of connection base on the rule of webservice
 */
public class CustomConnection {

//	private static final String URL = "https://nlunoti.azurewebsites.net/NongLamAnnounceService/service/";
	private static final String URL = "https://10.0.2.2:8000/rest";

	public static boolean makeGETConnection(final Activity activity, URLSuffix suffix, final NameOfResources nameOfResources) {
		RequestQueue queue = Volley.newRequestQueue(activity.getApplicationContext());
		String url = URL + suffix.getSuffix();
		StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						SharedPreferences sp = activity.getSharedPreferences(MemoryName.TEMP_DATA.toString(), Context.MODE_PRIVATE);
						SharedPreferences.Editor editor = sp.edit();
						editor.putString(nameOfResources.toString(), response);
						editor.apply();


					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(activity.getApplicationContext(), activity.getText(R.string.error_connection), Toast.LENGTH_LONG);
			}
		});
		queue.add(stringRequest);
		return true;
	}

	public static void jsonGETConnection(final Activity activity, URLSuffix suffix, final String nameOfResources) {
		String url = URL + suffix.getSuffix();

		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
				(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						SharedPreferences sp = activity.getSharedPreferences(MemoryName.TEMP_DATA.toString(), Context.MODE_PRIVATE);
						SharedPreferences.Editor editor = sp.edit();
						editor.putString(nameOfResources, response.toString());
						editor.apply();
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {

					}
				});
	}

	public static boolean makeGETConnectionWithParameter(final Activity activity, URLSuffix suffix, final NameOfResources nameOfResources, String... parameters) {
		RequestQueue queue = Volley.newRequestQueue(activity.getApplicationContext());
		String url = URL + suffix.getSuffix();
		for (String i : parameters) {
			url += "/" + i;
		}
		StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				SharedPreferences sp = activity.getSharedPreferences(MemoryName.TEMP_DATA.toString(), Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = sp.edit();
				editor.putString(nameOfResources.toString(), response);
				editor.apply();
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(activity.getApplicationContext(), activity.getText(R.string.error_connection), Toast.LENGTH_LONG);
			}
		});
		queue.add(stringRequest);
		return true;
	}

    public static boolean makeDELETEConnection(final Activity activity, URLSuffix suffix, final String nameOfResources) {
        RequestQueue queue = Volley.newRequestQueue(activity.getApplicationContext());
        String url = URL + suffix.getSuffix();
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                SharedPreferences sp = activity.getSharedPreferences(MemoryName.TEMP_DATA.toString(), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(nameOfResources.toString(), response);
                editor.apply();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity.getApplicationContext(), activity.getText(R.string.error_connection), Toast.LENGTH_LONG);
            }
        });
        queue.add(stringRequest);
        return true;
    }

    public static boolean makeDELETEConnectionWithParameter(final Activity activity, URLSuffix suffix, final NameOfResources nameOfResources, String... parameters) {
		RequestQueue queue = Volley.newRequestQueue(activity.getApplicationContext());
		String url = URL + suffix.getSuffix();
        for (String i : parameters) {
            url += "/" + i;
        }
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				SharedPreferences sp = activity.getSharedPreferences(MemoryName.TEMP_DATA.toString(), Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = sp.edit();
				editor.putString(nameOfResources.toString(), response);
				editor.apply();
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(activity.getApplicationContext(), activity.getText(R.string.error_connection), Toast.LENGTH_LONG);
			}
		});
        queue.add(stringRequest);
        return true;
	}

	public static boolean makePOSTConnectionWithParameter(final Activity activity, URLSuffix suffix, final NameOfResources nameOfResources, String... parameters) {
		RequestQueue queue = Volley.newRequestQueue(activity.getApplicationContext());
		String url = URL + suffix.getSuffix();
		for (String i : parameters) {
			url += "/" + i;
		}
		StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				SharedPreferences sp = activity.getSharedPreferences(MemoryName.TEMP_DATA.toString(), Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = sp.edit();
				editor.putString(nameOfResources.toString(), response);
				editor.apply();
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(activity.getApplicationContext(), activity.getText(R.string.error_connection), Toast.LENGTH_LONG);
			}
		});
		queue.add(stringRequest);
		return true;
	}

	public static void makePUTConnectionWithParameter(final Activity activity, URLSuffix suffix, final NameOfResources nameOfResources, String... parameters) {
		RequestQueue queue = Volley.newRequestQueue(activity.getApplicationContext());
		String url = URL + suffix.getSuffix();
		for (String i : parameters) {
			url += "/" + i;
		}
		StringRequest stringRequest = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				SharedPreferences sp = activity.getSharedPreferences(MemoryName.TEMP_DATA.toString(), Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = sp.edit();
				editor.putString(nameOfResources.toString(), response);
				editor.apply();
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(activity.getApplicationContext(), activity.getText(R.string.error_connection), Toast.LENGTH_LONG);
			}
		});
		queue.add(stringRequest);
	}

	/**
	 * This enum contain suffix of url for calling service from webservice
	 * Pattern for name of URL Suffix is type of connection + service name + behavior and using '_' for spacing
	 */
	public enum URLSuffix {
		GET_ANNOUNCE_RECENT("announce/recent"),
		GET_USER_LOGIN("user/login"),
		GET_ANNOUNCE_ALL("announce/all"),
		GET_NAME_BY_ID("findname"),
		POST_JOIN_GROUP("group/join"),
		GET_GROUP_LIST("user/dsgroup"),
		POST_ANNOUNCE_POST("announce/add"),
		GET_USER_INFO("user/info"),
		GET_PASSWORD_RESET("user/resetpass"),
		PUT_PASSWORD_CHANGE("user/changepass"),
        GET_ANNOUNCE_GET_BY_USER_ID("announce/user"),
        DELETE_ANNOUNCE_DELETED("announce/del");
		URLSuffix(String suffix) {
			this.suffix = suffix;
		}

		private String suffix;

		public String getSuffix() {
			return suffix;
		}
	}
}

