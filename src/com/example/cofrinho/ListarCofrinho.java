package com.example.cofrinho;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

public class ListarCofrinho extends ListActivity {

	protected static final int INSERIR_EDITAR = 1;

	public static CofrinhoRepositorio repositorio;

	private List<Cofrinho> cofrinhos;

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		repositorio = new CofrinhoRepositorio(this);
		atualizarLista();
	}

	protected void atualizarLista() {
		cofrinhos = repositorio.listarCofrinhos();
		setListAdapter(new CofrinhoListAdapter(this, cofrinhos));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, INSERIR_EDITAR, 0, "Novo");
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case INSERIR_EDITAR:
			startActivityForResult(new Intent(this, CadastrarCofrinho.class),
					INSERIR_EDITAR);
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

