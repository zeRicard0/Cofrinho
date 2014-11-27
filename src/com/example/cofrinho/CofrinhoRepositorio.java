package com.example.cofrinho;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.cofrinho.Cofrinho.Cofrinhos;

public class CofrinhoRepositorio {

	private static final String SCRIPT_DATABASE_DELETE = "DROP TABLE IF EXISTS cofrinho";

	private static final String[] SCRIPT_DATABASE_CREATE = new String[] {
			"create table cofrinho (_id integer primary key autoincrement, nome text not null, "
					+ "valor double not null, data integer not null, tipo int not null);",
			"insert into cofrinho (nome,valor,data,tipo) values('Teste1',100.00, 61120141931, 0);" };

	private static final String NOME_BANCO = "cofrinho";

	private static final int VERSAO_BANCO = 1;

	public static final String COFRINHO_TABELA = "cofrinho";

	protected SQLiteDatabase db;

	public CofrinhoRepositorio(Context ctx) {
		db = new SQLiteHelper(ctx, CofrinhoRepositorio.NOME_BANCO,
				CofrinhoRepositorio.VERSAO_BANCO,
				CofrinhoRepositorio.SCRIPT_DATABASE_CREATE,
				CofrinhoRepositorio.SCRIPT_DATABASE_DELETE)
				.getWritableDatabase();

	}

	public long salvar(Cofrinho cofrinho) {

		if (cofrinho.id == 0) {

			return inserir(cofrinho);
		}

		atualizar(cofrinho);
		return cofrinho.id;
	}

	private long inserir(Cofrinho cofrinho) {
		ContentValues values = new ContentValues();

		values.put(Cofrinhos.NOME, cofrinho.nome);
		values.put(Cofrinhos.VALOR, cofrinho.valor);
		values.put(Cofrinhos.DATA, cofrinho.data);
		values.put(Cofrinhos.TIPO, cofrinho.tipo);

		return inserir(values);

	}

	private long inserir(ContentValues values) {
		return db.insert(COFRINHO_TABELA, "", values);
	}

	private int atualizar(Cofrinho cofrinho) {
		ContentValues values = new ContentValues();

		values.put(Cofrinhos.NOME, cofrinho.nome);
		values.put(Cofrinhos.VALOR, cofrinho.valor);
		values.put(Cofrinhos.DATA, cofrinho.data);
		values.put(Cofrinhos.TIPO, cofrinho.tipo);

		String _id = String.valueOf(cofrinho.id);

		String where = Cofrinhos._ID + "=?";
		String[] whereArgs = new String[] { _id };

		return atualizar(values, where, whereArgs);

	}

	public int atualizar(ContentValues values, String where, String[] whereArgs) {
		return db.update(COFRINHO_TABELA, values, where, whereArgs);
	}

	public int delete(long id) {
		String where = Cofrinhos._ID + "=?";
		String _id = String.valueOf(id);
		String[] whereArgs = new String[] { _id };

		return delete(where, whereArgs);
	}

	public int delete(String where, String[] whereArgs) {
		return db.delete(COFRINHO_TABELA, where, whereArgs);
	}
	
	public void deleteAll(){
		
		db.execSQL("delete from " + COFRINHO_TABELA);
		
	}

	public Cursor getCursor() {
		try {
			return db.query(COFRINHO_TABELA, Cofrinho.colunas, null, null,
					null, null, Cofrinhos.DATA, null );
		} catch (SQLException e) {
			return null;
		}
	}

	public List<Cofrinho> listarCofrinhos() {

		Cursor c = getCursor();
		List<Cofrinho> cofrinhos = new ArrayList<Cofrinho>();

		if (c.moveToFirst()) {
			int idxId = c.getColumnIndex(Cofrinhos._ID);
			int idxNome = c.getColumnIndex(Cofrinhos.NOME);
			int idxValor = c.getColumnIndex(Cofrinhos.VALOR);
			int idxData = c.getColumnIndex(Cofrinhos.DATA);
			int idxTipo = c.getColumnIndex(Cofrinhos.TIPO);

			do {

				Cofrinho cofrinho = new Cofrinho();
				cofrinho.id = c.getLong(idxId);
				cofrinho.nome = c.getString(idxNome);
				cofrinho.valor = c.getDouble(idxValor);
				cofrinho.data = c.getLong(idxData);
				cofrinho.tipo = c.getInt(idxTipo);

				cofrinhos.add(cofrinho);

			} while (c.moveToNext());
		}
		
		return cofrinhos;
	}
	
	public void fechar(){
		if (db != null){
			db.close();
		}
	}

} // fim classe
