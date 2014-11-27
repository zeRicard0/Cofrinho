package com.example.cofrinho;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.cofrinho.Cofrinho.Cofrinhos;

public class CadastrarCofrinho extends Activity {

	static final int RESULT_SALVAR = 1;
	static final int RESULT_EXCLUIR = 2;
	static final int DATE_DIALOG_ID = 3;
	static final int TIME_DIALOG_ID = 4;

	public int ano, mes, dia, hora, minuto;
	private EditText campoNome;
	private EditText campoValor;
	private EditText campoData;
	private EditText campoHora;
	private Button botaoData;
	private Button botaoHora;
	private TableRow trHora;
	private Spinner campoTipo;
	private CheckBox cbLembrete;
	private Long id;

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.activity_cadastro_cofrinho);

		Calendar diaAtual = Calendar.getInstance();

		ano = diaAtual.get(Calendar.YEAR);
		mes = diaAtual.get(Calendar.MONTH)+1;
		dia = diaAtual.get(Calendar.DAY_OF_MONTH);

		campoNome = (EditText) findViewById(R.id.campoNome);
		campoValor = (EditText) findViewById(R.id.campoValor);
		campoData = (EditText) findViewById(R.id.campoData);
		campoHora = (EditText) findViewById(R.id.campoHora);
		campoData.setText(pad(dia) + "/" + pad(mes) + "/" + ano);
		botaoData = (Button) findViewById(R.id.botaoData);
		botaoHora = (Button) findViewById(R.id.botaoHora);

		trHora = (TableRow) findViewById(R.id.trHora);
		trHora.setVisibility(4);

		campoTipo = (Spinner) findViewById(R.id.spinnerTipo);
		cbLembrete = (CheckBox) findViewById(R.id.cbLembrete);

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.tipoMovimentacao,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		campoTipo.setAdapter(adapter);

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

		botaoHora.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(TIME_DIALOG_ID);

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

		cbLembrete
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {

						if (isChecked) {
							trHora.setVisibility(0);

							Calendar horaAtual = Calendar.getInstance();

							hora = horaAtual.get(Calendar.HOUR_OF_DAY);
							minuto = horaAtual.get(Calendar.MINUTE);

							campoHora.setText(pad(hora) + ":" + pad(minuto));

						} else {
							trHora.setVisibility(4);
						}

					}
				});

	}

	@Override
	protected Dialog onCreateDialog(int id) {

		Calendar hoje = Calendar.getInstance();
		ano = hoje.get(Calendar.YEAR);
		mes = hoje.get(Calendar.MONTH);
		dia = hoje.get(Calendar.DAY_OF_MONTH);

		hora = hoje.get(Calendar.HOUR_OF_DAY);
		minuto = hoje.get(Calendar.MINUTE);

		if (id == 3) {
			return new DatePickerDialog(this, mDateSetListener, ano, mes, dia);
		} else {
			return new TimePickerDialog(this, mTimeSetListener, hora, minuto, false);
		}

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

	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int horaSelec, int minutoSelec) {

			hora = horaSelec;
			minuto = minutoSelec;

			campoHora.setText(pad(hora) + ":" + pad(minuto));

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

		Calendar c = Calendar.getInstance();
		c.set(ano, mes, dia, hora, minuto);
		cofrinho.data = c.getTimeInMillis();

		cofrinho.tipo = (int) campoTipo.getSelectedItemPosition();

		String aux = campoValor.getText().toString();

		if (aux.isEmpty()) {
			cofrinho.valor = 0.0;
		} else {
			cofrinho.valor = Double.parseDouble(aux);
		}

		if (!cofrinho.nome.isEmpty()) {

			if (cbLembrete.isChecked()) {
				agendarNotificacao(cofrinho);
			}

			cofrinho.id = salvarCofrinho(cofrinho);
			setResult(RESULT_OK, new Intent());
			Toast.makeText(this, "Movimenta��o adicionada com sucesso",
					Toast.LENGTH_SHORT).show();
			

			finish();

		} else {

			Toast.makeText(this,
					"É necessário informar um nome para a transação!",
					Toast.LENGTH_SHORT).show();

		}
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

	public void agendarNotificacao(Cofrinho cofrinho) {
//		Toast.makeText(this, "Lembrete salvo com sucesso! (Not yet!)", Toast.LENGTH_SHORT).show();
		
		Intent intencaoAgendar = new Intent("AGENDAR");
		intencaoAgendar.putExtra("cofrinho", cofrinho);
		
		PendingIntent pIntent = PendingIntent.getBroadcast(this, 0, intencaoAgendar, PendingIntent.FLAG_UPDATE_CURRENT);
		
		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		
		alarmManager.set(AlarmManager.RTC_WAKEUP, cofrinho.data, pIntent);
		
	}

}// fim classe
