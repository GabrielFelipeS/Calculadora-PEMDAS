package calculadora.controller;

import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class CalculadoraController {

	private boolean operadorOuPontoNoFim = false;
	private boolean pontoIncluido = false;
	private boolean numeroNegativo	= false;
	private int quantidadeParentesesAbertos = 0;
	private int quantidadeParentesesFechados = 0;
	private int qtdOperadores = 0;
	private JTextField display;
	
	public CalculadoraController(JTextField display) {
		this.display = display;
	}
	
	
	public void fecharParenteses() {
		
	}
	
	
	public void igual() {
		
		if(operadorOuPontoNoFim || quantidadeParentesesAbertos > quantidadeParentesesFechados) {
			return;
		}
		
		String texto = display.getText();
		System.err.println(texto);
		
		if(texto.indexOf('(') != -1) {
			texto = parentesesSolution(texto);
			display.setText(texto);
		}
		
		ArrayList<Double> listaNumeros = pendas();
		
		String resposta = resposta(listaNumeros);
		
		int tamanho = (resposta.length() <= 13)? resposta.length() : 13;
		display.setText(resposta.substring(0, tamanho));
	}
	
	//9+(9+9+(5*2))
	private String parentesesSolution(String texto) {
		int indexParenteses = texto.indexOf("(");
		String parenteses = null;
		String resultado = null;
		
		if(indexParenteses != -1) {
			parenteses = texto.substring(indexParenteses + 1);
			resultado = parentesesSolution(parenteses);
			texto = texto.replace("(" + parenteses, resultado);
			return texto;
		}
		
		String fechamentoParenteses = texto.substring(0, texto.indexOf(")"));
		
		ArrayList<Double> listaNumeros = pendas(fechamentoParenteses);
		
		String resposta = resposta(listaNumeros);
		
		int tamanho = (resposta.length() <= 13)? resposta.length() : 13;
		
		return resposta.substring(0, tamanho);
	}
	
	private ArrayList<Double> pendas() {
		return pendas(display.getText());
	}

	
	private ArrayList<Double> pendas(String texto) {
		ArrayList<Double> listaNumeros = new ArrayList<Double>();
		ArrayList<Character> listaOperadores = new ArrayList<Character>();
		dividirEntreArryas(listaNumeros, listaOperadores, texto);
		while(!listaOperadores.isEmpty()) {
			int index = indexOperadores(listaNumeros, listaOperadores);
			double conta = conta(listaNumeros, listaOperadores, index);
			
			if(!listaOperadores.isEmpty()) {
				listaNumeros.set(index, conta);
				listaNumeros.remove(index + 1);
				listaOperadores.remove(index);
			}
			
		}
		
		return listaNumeros;
	}

	String resposta(ArrayList<Double> listaNumeros) {
		String resposta = Double.toString(listaNumeros.get(0));
		System.out.println("Resposta: " + resposta);
		
		if(resposta.substring(resposta.indexOf('.') + 1).replace("0", "").length() == 0) {
			int resp = Integer.parseInt(resposta.substring(0, resposta.indexOf('.')));
			System.out.println(resp);
			
			resposta = String.valueOf(resp);
			System.out.println(resposta);
		} else {
			pontoIncluido = true;		
		}	
	
		return resposta;
	}

	private void dividirEntreArryas(ArrayList<Double> listaNumeros, ArrayList<Character> listaOperadores, String texto) {
		String string = "";
		//String texto = display.getText();
		//System.out.println(texto);
		boolean operador = true;
		for(char c : texto.toCharArray()) {
			int hashCode = Character.hashCode(c);
			//System.out.println(hashCode);
			// Se maior significa que é um numero
			if((hashCode > 47 || hashCode == 46 || operador) && hashCode != 94) {
				string += c;
				operador = false;
			} else {
				listaNumeros.add(Double.parseDouble(string));
				listaOperadores.add(c);
				operador = true;
				string = "";
			}
		}
		listaNumeros.add(Double.parseDouble(string));
		
		System.out.print("Lista de numeros: ");
		for(Object c : listaNumeros.toArray()) {
			System.out.print(c + " ");
		}
		
		System.out.print("\nLista de operadores: ");
		for(Object c : listaOperadores.toArray()) {
			System.out.print(c + " ");
		}
	}
	
	private int indexOperadores(ArrayList<Double> listaNumeros, ArrayList<Character> listaOperadores) {
		int potenciaIndex = listaOperadores.indexOf('^');
		int multIndex = listaOperadores.indexOf('*');
		int divisaoIndex = listaOperadores.indexOf('/');
		int somaIndex = listaOperadores.indexOf('+');
		int subtracaoIndex = listaOperadores.indexOf('-');
		int index;
		
		
		if(potenciaIndex != -1) {
			index = potenciaIndex;
		} else if(multIndex != -1 || divisaoIndex != -1) {
			if(multIndex != -1 && (multIndex < divisaoIndex || divisaoIndex == -1)) {
				index = multIndex;
			} else {
				index = divisaoIndex;
			}
		} else {
			if(somaIndex != -1 && (somaIndex < subtracaoIndex || subtracaoIndex == -1)) {
				index = somaIndex;
			} else {
				index = subtracaoIndex;
			}
		}
		
		System.out.println("\nMultiIndex: " +  multIndex);
		System.out.println("DivIndex: " + divisaoIndex);
		System.out.println("SomaIndex: " +  somaIndex);
		System.out.println("SubtracaoIndex: " + subtracaoIndex);
		
		return index;
	}

	private double conta(ArrayList<Double> listaNumeros, ArrayList<Character> listaOperadores, int index) {
		double value1 = (double) listaNumeros.get(index);
		double value2 = (double) listaNumeros.get(index + 1);
		char operador = (char) listaOperadores.get(index);
		
		
		switch(operador) {
			case '*':
				return value1 * value2;
			case '/':
				if(value2 == 0) {
					JOptionPane.showMessageDialog(null, "Encontramos uma divisão por 0, por favor faça outra conta", "Divisao por zero", JOptionPane.WARNING_MESSAGE);
					display.setText("");
					listaOperadores.removeAll(listaOperadores);
					return 0;
				}
				return value1 / value2;
			case '-':
				return value1 - value2;
			case '+':
				return value1 + value2;
			case '^':
				return Math.pow(value1, value2);
		}
		
		return 0;
	}

	
}
