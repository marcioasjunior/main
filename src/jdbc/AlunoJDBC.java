package jdbc;


import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import entities.Aluno;

public class AlunoJDBC {

	Connection con;
	String sql;
	PreparedStatement pst;
	
	public void salvar(Aluno a) throws SQLException {
		
		try {
			con = db.getConexao();
			sql = "INSERT INTO aluno (nome, sexo, dt_nasc) VALUES (?,?, ?)";
			pst = con.prepareStatement(sql);
			pst.setString(1, a.getNome());
			pst.setString(2, a.getSexo());
			Date dataSql = Date.valueOf( a.getDt_nasc() );
			pst.setDate(3, dataSql);
			pst.executeUpdate();
			System.out.println("\nCadastro do aluno realizado com sucesso!");
			
		} catch (Exception e) {
			System.out.println(e);
		}
		finally {
			pst.close();
			db.closeConexao();
		}
	}
	
	public List<Aluno> listar() throws SQLException {
		    List<Aluno> alunos = new ArrayList<>();
		    
		    try {
		        con = db.getConexao();
		        sql = "SELECT * FROM aluno";
		        pst = con.prepareStatement(sql);
		        ResultSet rs = pst.executeQuery();
		        
		        while (rs.next()) {
		            Aluno aluno = new Aluno();
		            aluno.setId(rs.getInt("id"));
		            aluno.setNome(rs.getString("nome"));
		            aluno.setSexo(rs.getString("sexo"));
		            aluno.setDt_nasc(rs.getDate("dt_nasc").toLocalDate());
		            
		            alunos.add(aluno);
		        }
		        System.out.println("\nLista de alunos recuperada com sucesso!");
		        
		    } catch (Exception e) {
		        System.out.println(e);
		    } finally {
		        if (pst != null) pst.close();
		        /*if (con != null) db.closeConexao();*/
		    }
		    
		    return alunos;
		}	
	
	
	public void apagar(int id) throws SQLException, IOException {
        try {
            con = db.getConexao(); 
            sql = "DELETE FROM aluno WHERE id = ?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, id);
            int rowsAffected = pst.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("Aluno com ID " + id + " foi excluído com sucesso.");
            } else {
                System.out.println("Nenhum aluno encontrado com o ID " + id);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao excluir aluno: " + e.getMessage());
            throw e; 
        } finally {
        	if (pst != null) pst.close(); 
        }
    }

    private void closeResources() {
        try {
            if (pst != null) pst.close();
            if (con != null) db.closeConexao();
        } catch (SQLException e) {
            System.out.println("Erro ao fechar recursos: " + e.getMessage());
        }
    }

	
	public void alterar(Aluno a) throws SQLException, IOException {
	    Connection con = null;
	    PreparedStatement pst = null;

	    try {
	        con = db.getConexao();
	        if (con == null) {
	            System.out.println("Falha na conexão com o banco de dados.");
	            return;
	        }
	        
	        String sql = "UPDATE aluno SET nome = ?, sexo = ?, dt_nasc = ? WHERE id = ?";
	        pst = con.prepareStatement(sql);

	        pst.setString(1, a.getNome());
	        pst.setString(2, a.getSexo());
	        Date dataSql = Date.valueOf(a.getDt_nasc());
	        pst.setDate(3, dataSql);
	        pst.setInt(4, a.getId());

	        int rowsAffected = pst.executeUpdate();
	        if (rowsAffected > 0) {
	            System.out.println("Aluno com ID " + a.getId() + " foi atualizado com sucesso.");
	        } else {
	            System.out.println("Nenhum aluno encontrado com o ID " + a.getId());
	        }
	    } catch (SQLException e) {
	        System.out.println("Erro ao atualizar aluno: " + e.getMessage());
	        throw e;
	    } finally {
	        if (pst != null) {
	            try {
	                pst.close();
	            } catch (SQLException e) {
	                System.out.println("Erro ao fechar PreparedStatement: " + e.getMessage());
	            }
	        }
	        /*if (con != null) {
	            try {
	                db.closeConexao();
	            } catch (SQLException e) {
	                System.out.println("Erro ao fechar conexão: " + e.getMessage());
	            }
	        }*/
	    }
	}
}

