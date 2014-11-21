package com.example.cofrinho;

import java.io.Serializable;

import android.provider.BaseColumns;

public class Cofrinho implements Serializable {

	public static String[] colunas = new String[] { Cofrinhos._ID,
			Cofrinhos.NOME, Cofrinhos.VALOR, Cofrinhos.DATA, Cofrinhos.TIPO };

	public long id;
	public String nome;
	public double valor;
	public long data;
	public int tipo;

	public static final class Cofrinhos implements BaseColumns {

		private Cofrinhos() {

		}

		public static final String DEFAULT_SORT_ORDER = "_id ASC";
		public static final String NOME = "nome";
		public static final String VALOR = "valor";
		public static final String DATA = "data";
		public static final String TIPO = "tipo";
	}

	@Override
	public String toString() {

		return "Nome: " + nome + "Valor: " + valor + "Data: " + data + "Tipo: "
				+ tipo;
	}

}
