package com.uki121.hguidetemplate;


import android.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
  implements NavigationView.OnNavigationItemSelectedListener {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    //Element_fragment_main
    /*
    if (findViewById(R.id.fragment_container) != null) {
        if (savedInstanceState != null) {
            return;
        }
    }
    */
    //(tips)새 프레그먼트 생성
    NavListFragment fragMain = new NavListFragment();
    fragMain.setArguments(getIntent().getExtras());
    //(tips)액티비티가 인텐트의 특정 명령무에서 시작됐다면, 인텐트의 엑스트라를 인자로 프레그먼트에 전달한다.
    getSupportFragmentManager().beginTransaction()
      //(tips)프레그먼트 매니저 인스턴스의 beginTransaction() 호출
      .add(R.id.fragment_container,
        //(tips)프레그먼트 트랜젝션 인스턴스의 add 메소드를 호출하고 프레그먼트(R.id.fragMain)포함
        //하는 뷰의 리소스 id와 프레그먼트 클래스의 인스턴스(fragMain)를 전달
        fragMain).commit();

    //Element_floating Icon
    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
          .setAction("Action", null).show();
      }
    });
    //Element_navigation
    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
      this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    drawer.addDrawerListener(toggle);
    toggle.syncState();

    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
    navigationView.setNavigationItemSelectedListener(this);
  }

  @Override
  public void onBackPressed() {
    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawer(GravityCompat.START);
    } else {
      super.onBackPressed();
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
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

  @SuppressWarnings("StatementWithEmptyBody")
  @Override
  public boolean onNavigationItemSelected(MenuItem item) {
    // Handle navigation view item clicks here.
    int id = item.getItemId();
    FragmentManager fragmentManager = getFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

    //Todo : replace or add choose
    if (id == R.id.nav_scene1) {
      fragmentTransaction.add(R.id.fragment_container, FragmentScroll.newInstance()).commit();

    } else if (id == R.id.nav_scene2) {
      fragmentTransaction.add(R.id.fragment_container, new FragmentEdit()).commit();

    } else if (id == R.id.nav_scene3) {
      fragmentTransaction.add(R.id.fragment_container, new FragmentProcess()).commit();

    } else if (id == R.id.nav_scene4) {
      fragmentTransaction.add(R.id.fragment_container, new FragmentPopup()).commit();

    }
    /*
      Todo : This will be implemented by "list", not showing new fragment
      else if (id == R.id.nav_switch) {
      fragmentTransaction.add(R.id.fragment_container, ~).commit();
    } else if (id == R.id.nav_cache) {
      fragmentTransaction.add(R.id.fragment_container, ~).commit();
    }
    */
    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    drawer.closeDrawer(GravityCompat.START);
    return true;
  }
}
