package com.example.adminapi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class RoutesAct1 extends AppCompatActivity implements View.OnClickListener {

    int id=-1;
    public int ids;
    ListView listRoutesView;
    EditText editTen;
    EditText editSDT;
    Button btnThem,btnSua,btnXoa;
    ArrayList<extendroutes> arrExtendRoutes;
    CustomAdapter myadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.routes_act_1);
        anhxa();
        arrExtendRoutes=new ArrayList<extendroutes>();
        myadapter=new CustomAdapter(this,R.layout.item_layout,arrExtendRoutes);
        listRoutesView.setAdapter(myadapter);
        listRoutesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                id=position;
                ids=position;
                editTen.setText(arrExtendRoutes.get(position).getLocation());
            }
        });
        listRoutesView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                arrExtendRoutes.remove(position);
                myadapter.notifyDataSetChanged();
                Toast.makeText(RoutesAct1.this,"Da xoa Thanh cong",Toast.LENGTH_SHORT).show();
                return false;
            }
        });

    }
    private void anhxa(){
        listRoutesView=(ListView) findViewById(R.id.listRoutesView);
        editTen=(EditText) findViewById(R.id.edit_ten);
        btnThem=(Button) findViewById(R.id.btn_them);
        btnSua=(Button) findViewById(R.id.btn_sua);
        btnXoa=(Button) findViewById(R.id.btn_xoa);
        btnThem.setOnClickListener(this);
        btnSua.setOnClickListener(this);
        btnXoa.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_them:
                Toast.makeText(this,"Added",Toast.LENGTH_SHORT).show();
                String ten=editTen.getText().toString();
                extendroutes temp= new extendroutes(ten);
                arrExtendRoutes.add(temp);
                myadapter.notifyDataSetChanged();
                break;

            case R.id.btn_sua:
                break;
            case R.id.btn_xoa:
                break;
        }
    }
}
