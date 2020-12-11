package fragment

import adapter.DashboardRecyclerAdapter
import android.app.Activity
import android.app.AlertDialog
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.rohit.bookhub.R
import com.rohit.bookhub.model.Book
import com.rohit.bookhub.util.ConnectionManager
import org.json.JSONException


class DashboardFragment : Fragment() {

    lateinit var recyclerDashboard: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
//    lateinit var btnCheckInternet:Button

    /* bookList = arrayListOf<String>(
        "P.S. I Love You",
        "The Great Gatsby",
        "Anna Karenina",
        "Madame Bovary",
        "War and Peace",
        "Lolita",
        "MiddleMarch",
        "The Adventure of Huckleberry Finn",
        "Moby-Dick",
        "The Lord of the Rings"
    )*/

    lateinit var recyclerAdapter: DashboardRecyclerAdapter

    lateinit var progressLayout: RelativeLayout

    lateinit var progressBar: ProgressBar

//val bookInfoList = arrayListOf<Book>(
//    Book("P.S. I Love You","Cecelia Ahern","Rs. 299","4-5", R.drawable.ps_ily),
//    Book("The Greate Gatsby","F. Scott Fitzgerald","Rs. 199","4.5",R.drawable.great_gatsby),
//    Book("Anna Karenina","Leo Tolstoy","Rs. 399","4.5",R.drawable.anna_kare),
//    Book("Madame Bovary","Gustave Flaubert","Rs. 500","4.0",R.drawable.madame),
//    Book("War and Peace","Leo Tolstoy","Rs. 249","4.8",R.drawable.war_and_peace),
//    Book("Lolita","Vladimir Nabokov","Rs. 349","3.9",R.drawable.lolita),
//    Book("Middle March","George Elliot","Rs. 599","4.2",R.drawable.middlemarch),
//    Book("The Adventure of Huckleberry Finn","Mark Twain","Rs. 699","4.5",R.drawable.adventures_finn),
//    Book("Moby-Dick","Herman Melville","Rs. 499","4.5",R.drawable.moby_dick),
//    Book("The Lord of the Rings","J.R.R. Tolkien","Rs. 749","5.0",R.drawable.lord_of_rings)
//)
    val bookInfoList = arrayListOf<Book>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       val  view =  inflater.inflate(R.layout.fragment_dashboard, container, false)

        recyclerDashboard = view.findViewById(R.id.recyclerDashboard)


//        btnCheckInternet = view.findViewById(R.id.btnCheckInternet)

        progressLayout = view.findViewById(R.id.progressLayout)

        progressBar = view.findViewById(R.id.progressBar)

        progressLayout.visibility=View.VISIBLE

        /*btnCheckInternet.setOnClickListener {
            if (ConnectionManager().checkConnectivity(activity as Context)){
                val dialog = AlertDialog.Builder(activity as Context)
                dialog.setTitle("Success")
                dialog.setMessage("Internet Connection Found")
                dialog.setPositiveButton("OK"){text, listener->

                }

                dialog.setNegativeButton("Cancle"){text, listener->

                }
                dialog.create()
                dialog.show()
            }
            else{
                val dialog = AlertDialog.Builder(activity as Context)
                dialog.setTitle("Error")
                dialog.setMessage("Internet Connection Not Found")
                dialog.setPositiveButton("OK"){text, listener->

                }

                dialog.setNegativeButton("Cancle"){text, listener->

                }
                dialog.create()
                dialog.show()
            }
        }*/

        layoutManager = LinearLayoutManager(activity)

//        recyclerAdapter = DashboardRecyclerAdapter(activity as Context, bookInfoList)
//
//        recyclerDashboard.adapter = recyclerAdapter
//
//        recyclerDashboard.layoutManager = layoutManager
//
//        recyclerDashboard.addItemDecoration(
//            DividerItemDecoration(
//                recyclerDashboard.context,
//                (layoutManager as LinearLayoutManager).orientation))

        val queue = Volley.newRequestQueue(activity as Context)

        val url = "http://13.235.250.119/v1/book/fetch_books/"

        if(ConnectionManager().checkConnectivity(activity as Context)){

            val josnObjectRequest = object : JsonObjectRequest(Method.GET, url, null, Response.Listener {
                // Here we will handle the response
                try {
                    progressLayout.visibility = View.GONE
                    val success = it.getBoolean("success")

                    if(success){

                        val data = it.getJSONArray("data")
                        for(i in 0 until data.length()){
                            val bookJsonObject = data.getJSONObject(i)
                            val bookObject = Book(
                                bookJsonObject.getString("book_id"),
                                bookJsonObject.getString("name"),
                                bookJsonObject.getString("author"),
                                bookJsonObject.getString("rating"),
                                bookJsonObject.getString("price"),
                                bookJsonObject.getString("image")
                            )
                            bookInfoList.add(bookObject)
                            recyclerAdapter = DashboardRecyclerAdapter(activity as Context, bookInfoList)

                            recyclerDashboard.adapter = recyclerAdapter

                            recyclerDashboard.layoutManager = layoutManager

                            /*recyclerDashboard.addItemDecoration(
                                DividerItemDecoration(
                                    recyclerDashboard.context,
                                    (layoutManager as LinearLayoutManager).orientation
                                )
                            )*/
                        }
                    }else{
                        Toast.makeText(activity as Context, "Some error Occured !!!", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: JSONException){
                    Toast.makeText(activity as Context, "Some Unexpected error occured!!!", Toast.LENGTH_SHORT).show()
                }


            }, Response.ErrorListener {

                // Here we will handle the errors
                Toast.makeText(activity as Context,"Volley error occured!!",Toast.LENGTH_SHORT).show()
            }){
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-type"] = "application/json"
                    headers["token"] = "9bf534118365f1"
                    return headers
                }
            }

            queue.add(josnObjectRequest)
        }else{
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection Not Found")
            dialog.setPositiveButton("Open Settings"){text, listener->
            val settingsIntent = Intent(Settings.ACTION_WIFI_SETTINGS)
                startActivity(settingsIntent)
              activity?.finish()
            }

            dialog.setNegativeButton("Exit"){text, listener->
            ActivityCompat.finishAffinity((activity as Activity))
            }
            dialog.create()
            dialog.show()
        }


        return view

    }
}
