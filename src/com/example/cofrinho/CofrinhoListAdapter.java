package com.example.cofrinho;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CofrinhoListAdapter extends BaseAdapter {

	private Context context;
	private List<Cofrinho> list;

	public CofrinhoListAdapter(Context context, List<Cofrinho> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return list.get(position).id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Cofrinho c = list.get(position);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(context.LAYOUT_INFLATER_SERVICE);

		View view = inflater.inflate(R.layout.activity_lista_cofrinho, null);

		TextView nome = (TextView) view.findViewById(R.id.nome);
		nome.setText(c.nome);

		TextView valor = (TextView) view.findViewById(R.id.valor);
		valor.setText(String.valueOf(c.valor));

		// ARRUMA ISSO

		CadastrarCofrinho cof = new CadastrarCofrinho();

		TextView data = (TextView) view.findViewById(R.id.data);
		Calendar calData = Calendar.getInstance();
		calData.setTimeInMillis(c.data);
		data.setText(cof.pad(calData.get(Calendar.DAY_OF_MONTH)) + "/"
				+ cof.pad(calData.get(Calendar.MONTH) + 1) + "/"
				+ calData.get(Calendar.YEAR));

		TextView tipo = (TextView) view.findViewById(R.id.tipo);
		tipo.setText(String.valueOf(c.tipo));

		if (tipo.getText().toString().equals("0")) {
			valor.setTextColor(Color.GREEN);
		} else {
			valor.setTextColor(Color.RED);
		}

		tipo.setVisibility(4);

		return view;
	}

}
