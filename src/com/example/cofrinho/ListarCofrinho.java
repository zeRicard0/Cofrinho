package com.example.cofrinho;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ListarCofrinho extends ListActivity {

	protected static final int INSERIR_EDITAR = 1;
	protected static final int EXCLUIR_TODAS = 2;
	protected static final int MOSTRAR_TOTAL = 3;

	public static CofrinhoRepositorio repositorio;

	private List<Cofrinho> cofrinhos;

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		repositorio = new CofrinhoRepositorio(this);
		// campoTotal = (TextView) findViewById(R.id.valorTotal);
		// campoTotal.setText(String.valueOf("" + calcularTotal()));
		atualizarLista();

	}

	protected void atualizarLista() {
		cofrinhos = repositorio.listarCofrinhos();
		setListAdapter(new CofrinhoListAdapter(this, cofrinhos));

	}

	protected void excluirTodas() {
		repositorio.deleteAll();
		atualizarLista();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, INSERIR_EDITAR, 0, "Novo");
		menu.add(0, EXCLUIR_TODAS, 0, "Excluir todas");
		menu.add(0, MOSTRAR_TOTAL, 0, "Mostrar total");
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case INSERIR_EDITAR:
			startActivityForResult(new Intent(this, CadastrarCofrinho.class),
					INSERIR_EDITAR);
			break;
		case EXCLUIR_TODAS:

			excluirTodas();

			break;

		case MOSTRAR_TOTAL:

			Toast.makeText(this, "Total: " + calcularTotal(),
					Toast.LENGTH_SHORT).show();

			break;

		}
		return true;
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		deletarCofrinho((int) id);
		atualizarLista();
	}

	protected void deletarCofrinho(int id) {
		ListarCofrinho.repositorio.delete(id);
		ListarCofrinho.repositorio.listarCofrinhos();
	}

	protected double calcularTotal() {

		double total = 0.0;

		double receitas = 0.0;
		double despesas = 0.0;

		for (int i = 0; i < cofrinhos.size(); i++) {

			if (cofrinhos.get(i).tipo == 0) {
				receitas += cofrinhos.get(i).valor;
			} else {
				despesas += cofrinhos.get(i).valor;
			}

		}

		total = receitas - despesas;

		return total;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent it) {
		super.onActivityResult(requestCode, resultCode, it);

		if (resultCode == RESULT_OK) {
			atualizarLista();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		repositorio.fechar();
	}

}// fim classe

