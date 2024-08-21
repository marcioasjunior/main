package apllication;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

import entities.Aluno;
import jdbc.AlunoJDBC;


public class Program {

	public static void main(String[] args) {
		
		try {
        	
            int opcao = 0;
            Scanner console = new Scanner(System.in);
            
            do {
            	System.out.println("####### Menu #######"
            						+ "\n1 - Cadastrar"
            						+ "\n2 - Listar"
            						+ "\n3 - Alterar"
            						+ "\n4 - Excluir"
            						+ "\n5 - Sair");
            	System.out.println("\n\tOpcao:");
            	opcao = Integer.parseInt(console.nextLine());
            	
            	if (opcao == 1) {
            	
            		Aluno a = new Aluno();
            		AlunoJDBC acao = new AlunoJDBC();
            		
            		System.out.println("\n ### Cadastrar Aluno ### \n\r");
            		
            		System.out.print("Nome: ");
            		a.setNome(console.nextLine());
            		
            		System.out.print("Sexo: ");
            		a.setSexo(console.nextLine());
            	
            		System.out.print("Data de Nascimento (dd-mm-aaaa): ");
            		DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            		a.setDt_nasc( LocalDate.parse(console.nextLine(), formato) );
            		
            		acao.salvar(a);
            		console.nextLine();
            		System.out.println("\n\n\n\n");
            	}
            	
            	if (opcao == 2) {
            		
            		 AlunoJDBC acao = new AlunoJDBC();
            		
            		System.out.println("\n ### Listar Alunos ### \n\r");
                    List<Aluno> alunos = acao.listar();
                    for (Aluno aluno : alunos) {
                        System.out.println("Nome: " + aluno.getNome());
                        System.out.println("Sexo: " + aluno.getSexo());
                        System.out.println("Data de Nascimento: " + aluno.getDt_nasc());
                        System.out.println("-----------------------------");
                    }
                }
            	if (opcao == 3) {
            		AlunoJDBC acao = new AlunoJDBC();
            	    
            	    System.out.print("Digite o ID do aluno a ser alterado: ");
            	    int id;
            	    
            	    try {
            	        id = Integer.parseInt(console.nextLine());
            	    } catch (NumberFormatException e) {
            	        System.out.println("ID inválido. Deve ser um número inteiro.");
            	        return;
            	    }
            	    
            	    try {
            	        List<Aluno> alunos = acao.listar();
            	        
            	        Aluno alunoExistente = alunos.stream()
            	            .filter(a -> a.getId() == id)
            	            .findFirst()
            	            .orElse(null);
            	        
            	        if (alunoExistente != null) {
            	            System.out.println("\n ### Alterar Aluno ### \n");
            	            
            	            System.out.print("Novo Nome (atual: " + alunoExistente.getNome() + "): ");
            	            String novoNome = console.nextLine();
            	            alunoExistente.setNome(novoNome);
            	            
            	            System.out.print("Novo Sexo (atual: " + alunoExistente.getSexo() + "): ");
            	            String novoSexo = console.nextLine();
            	            alunoExistente.setSexo(novoSexo);
            	            
            	            LocalDate novaDataNascimento = null;
            	            while (novaDataNascimento == null) {
            	                System.out.print("Nova Data de Nascimento (dd/MM/yyyy) (atual: " + alunoExistente.getDt_nasc() + "): ");
            	                String inputData = console.nextLine();
            	                DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            	                
            	                try {
            	                    novaDataNascimento = LocalDate.parse(inputData, formato);
            	                } catch (DateTimeParseException e) {
            	                    System.out.println("Data inválida. Use o formato dd/MM/yyyy.");
            	                }
            	            }
            	            alunoExistente.setDt_nasc(novaDataNascimento);
            	            
            	            try {
            	                acao.alterar(alunoExistente);
            	                System.out.println("Aluno atualizado com sucesso.");
            	            } catch (SQLException | IOException e) {
            	                System.out.println("Erro ao atualizar aluno: " + e.getMessage());
            	            }
            	        } else {
            	            System.out.println("Aluno com ID " + id + " não encontrado.");
            	        }
            	    } catch (SQLException e) {
            	        System.out.println("Erro ao listar alunos: " + e.getMessage());
            	    }
            	}
            	
            	if (opcao == 4) {
            		  AlunoJDBC acao = new AlunoJDBC();
            		    
            		    System.out.print("Digite o ID do aluno a ser excluído: ");
            		    int id = Integer.parseInt(console.nextLine());
            		    
            		    try {
            		        acao.apagar(id);
            		    } catch (SQLException | IOException e) {
            		        System.out.println("Erro ao excluir aluno: " + e.getMessage());
            		    }
            		
            	}
            	
            } while(opcao != 5);
            
        } catch (Exception e){
            System.out.println("Erro: " + e);
        }
	} 
}
