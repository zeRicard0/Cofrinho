package com.example.cofrinho;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.cofrinho.Cofrinho.Cofrinhos;

public class CadastrarCofrinho extends Activity {

	static final int RESULT_SALVAR = 1;
	static final int RESULT_EXCLUIR = 2;
	static final int DATE_DIALOG_ID = 0;

	public int ano, mes, dia;
	private EditText campoNome;
	private EditText campoValor;
	private EditText campoData;
	private EditText campoTipo;
	private Button botaoData;
	private Long id;

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.activity_cadastro_cofrinho);

		campoNome = (EditText) findViewById(R.id.campoNome);
		campoValor = (EditText) findViewById(R.id.campoValor);
		campoData = (EditText) findViewById(R.id.campoData);
		campoData.setText(pad(dia) + "/" + pad(mes) + "/" + ano);
		botaoData = (Button) findViewById(R.id.botaoData);
		campoTipo = (EditText) findViewById(R.id.campoTipo);

		id = null;

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			id = extras.getLong(Cofrinhos._ID);

			if (id != null) {
				Log.d("Erro", "DEU PAU");
			}
		}

		botaoData.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);
			}
		});

		ImageButton btCancelar = (ImageButton) findViewById(R.id.btCancelar);
		btCancelar.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});

		ImageButton btSalvar = (ImageButton) findViewById(R.id.btSalvar);
		btSalvar.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				salvar();
			}
		});
	}

	@Override
	protected Dialog onCreateDialog(int id) {

		Calendar hoje = Calendar.getInstance();
		ano = hoje.get(Calendar.YEAR);
		mes = hoje.get(Calendar.MONTH);
		dia = hoje.get(Calendar.DAY_OF_MONTH);
		return new DatePickerDialog(this, mDateSetListener, ano, mes, dia);
	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int anoSelec, int mesSelec,
				int diaSelec) {
			dia = diaSelec;
			mes = mesSelec + 1;
			ano = anoSelec;

			campoData.setText(pad(dia) + "/" + pad(mes) + "/" + ano);
		}
	};

	public String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}

	@Override
	protected void onPause() {
		super.onPause();
		setResult(RESULT_CANCELED);
		finish();
	}

	public void salvar() {

		Cofrinho cofrinho = new Cofrinho();

		cofrinho.nome = campoNome.getText().toString();
		cofrinho.valor = Double.parseDouble(campoValor.getText().toString());

		Calendar c = Calendar.getInstance();
		c.set(ano, mes - 1, dia);
		cofrinho.data = c.getTimeInMillis();

		cofrinho.tipo = Integer.parseInt(campoTipo.getText().toString());

		cofrinho.id = salvarCofrinho(cofrinho);
		setResult(RESULT_OK, new Intent());

		finish();
	}

	protected long salvarCofrinho(Cofrinho cofrinho) {
		return ListarCofrinho.repositorio.salvar(cofrinho);
	}

	public void excluir() {
		if (id != null) {
			excluirCofrinho(id);
		}
	}

	protected void excluirCofrinho(long id) {
		ListarCofrinho.repositorio.delete(id);
	}

}// fim classe
