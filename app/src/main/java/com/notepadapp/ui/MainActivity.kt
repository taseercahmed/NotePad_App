package com.notepadapp.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.notepadapp.Adapter.NoteAdapter
import com.notepadapp.Database.DatabaseAccess
import com.notepadapp.Listener.Listener
import com.notepadapp.Model.Note
import com.notepadapp.R
import com.notepadapp.ViewModel.NoteViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(),
    Listener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var noteList: ArrayList<Note>
    private lateinit var searchView:SearchView
    private lateinit var dbaccess:DatabaseAccess

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        setSupportActionBar(toolbar)

        noteAdapter = NoteAdapter(applicationContext, ArrayList<Note>(), this)
        initialiseRecyclerView()
        val databaseAccess = DatabaseAccess.getInstance(this)
        databaseAccess.open()
        databaseAccess.duas.forEach {
            Log.i("112233dua",it+" dua")
        }
//        val quotes = databaseAccess.surahNames
//        quotes.forEach {
//            Log.e("1122334surah",it)
//        }
        databaseAccess.close()

        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        noteViewModel.getCardsData(this)?.observe(this, Observer {
            noteAdapter.setData(it as ArrayList<Note>)
            noteList = it
        })

        //dbaccess=DatabaseAccess.getInstance(applicationContext)


        floatingActionButton.setOnClickListener {
            val intent = Intent(this, AnotherActivity::class.java)
            startActivity(intent)
        }

        val itemTouchHelper=ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

    }

    private fun initialiseRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
            adapter = noteAdapter


        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menus, menu)
        val search= menu?.findItem(R.id.searchItems)
        searchView=search?.actionView as SearchView
        searchView.isSubmitButtonEnabled = true
        searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(query != null)
                    getItemsFromDB(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText != null)
                    getItemsFromDB(newText)
                return true
            }

        })
        return true
    }
     private fun getItemsFromDB(data:String)
    {
      var searchText=data
       searchText="%$data%"
       noteViewModel.search(this,searchText)?.observe(this, Observer {
           Log.d("main", "$it")
           noteAdapter.setData(it as ArrayList<Note>)
       })
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.setting -> {
            }
            R.id.list -> {
            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClickListener(position: Int) {
       val intent=Intent(this, UpdateActivity::class.java)
        intent.putExtra("note",noteList[position])
        startActivity(intent)
    }
     val simpleCallback= object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
         override fun onMove(
             recyclerView: RecyclerView,
             viewHolder: RecyclerView.ViewHolder,
             target: RecyclerView.ViewHolder
         ): Boolean {
             return true
         }

         override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
             val position=viewHolder.adapterPosition
             val note=noteList[position]
             when(direction)
             {
                 ItemTouchHelper.RIGHT->{
                     noteViewModel.delete(this@MainActivity,note)
                     noteAdapter.notifyDataSetChanged()
                 }
                 ItemTouchHelper.LEFT->{
                     noteViewModel.delete(this@MainActivity,note)
                     noteAdapter.notifyDataSetChanged()
                 }
             }
         }

     }

}