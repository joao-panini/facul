import java.util.*;
import redis.clients.jedis.Jedis; 

public class RedisJava { 
   public static void main(String[] args) { 
      //Conectando no Redis server -  localhost 
      Jedis jedis = new Jedis("localhost"); 
      System.out.println("Connecção com o servidor"); 
      //Testando se o servidor está execuando 
      System.out.println("Servidor está executando: "+jedis.ping()); 

      //Inserindo os dados em uma estrutura do tipo "string" 
      //jedis.set("ChaveExemplo", "Criando o primeiro conjunto chave-valor !"); 
      // Recuperando os dados e mostrando na tela 
      //System.out.println("Lendo o valor guardado na chave : "+ jedis.get("ChaveExemplo")); 
   

      //Inserindo os dados em uma estrutura do tipo Lista "list"
      //jedis.lpush("ListaExemplo", "Redis"); 
      //jedis.lpush("ListaExemplo", "Aula"); 
      //jedis.lpush("ListaExemplo", "Trabalho de BD"); 
      //  Recuperando os dados e mostrando na tela 
     // List<String> list = jedis.lrange("ListaExemplo", 0 ,100); 
      
      //for(int i = 0; i<list.size(); i++) { 
       //  System.out.println("Valor guardado na lista : "+list.get(i)); 
      //} 
      Scanner writerop = new Scanner(System.in);
      Scanner writer = new Scanner(System.in);
      Scanner writerApostas = new Scanner(System.in);
      int op;
      
      // Variaveis usuario
      String apelido;
      String nome;
      String datanasc;
      String genero;
      String pais;
      String estado;
      String cidade;
      String rua;
      String complemento;
      String codigopostal;
      
      //Variaveis aposta
      int rodada;
      int partida;
      
      System.out.println("---- MENU ----");
      System.out.println("1 - CADASTRO USUARIO");
      System.out.println("2 - APOSTAR");
      op = writerop.nextInt();
      
      switch(op) {
      case 1:
	      System.out.println("Insira seu apelido:");
	      
	      apelido = writer.nextLine();
	      jedis.zadd("APELIDO",1,apelido);
	      jedis.hset("USUARIO:"+apelido," APELIDO ", apelido);
	      
	      System.out.println("Insira seu nome completo:");
	      nome = writer.nextLine();
	      jedis.hset("USUARIO"+apelido," NOME ", nome);
	      
	      System.out.println("Insira sua data de nascimento:");
	      datanasc = writer.nextLine();
	      jedis.hset("USUARIO"+apelido," DATA_NASCIMENTO ", datanasc);
	     
	      System.out.println("Insira seu genero(M/F):");
	      genero = writer.nextLine();
	      jedis.hset("USUARIO"+apelido," GENERO ", genero);
	     
	      System.out.println("Insira seu pais:");
	      pais = writer.nextLine();
	      jedis.hset("USUARIO"+apelido," PAIS ", pais);
	      
	      System.out.println("Insira seu estado:");
	      estado = writer.nextLine();
	      jedis.hset("USUARIO"+apelido," ESTADO ", estado);
	      
	      System.out.println("Insira o nome da sua cidade:");
	      cidade = writer.nextLine();
	      jedis.hset("USUARIO"+apelido," CIDADE ", cidade);
	      
	      System.out.println("Insira o nome da sua rua:");
	      rua = writer.nextLine();
	      jedis.hset("USUARIO"+apelido," RUA ", rua);
	      
	      System.out.println("Insira o complemento:");
	      complemento = writer.nextLine();
	      jedis.hset("USUARIO"+apelido," COMPLEMENTO ", complemento);
	      
	      System.out.println("Insira o c�digo postal:");
	      codigopostal = writer.nextLine();
	      jedis.hset("USUARIO"+apelido," CODIGO_POSTAL ", codigopostal);
	      
	      writer.close();
	      break;

      case 2:
    	  System.out.println("Insira a rodada que deseja apostar:");
    	  
    	  System.out.println("Insira a partida que deseja apostar:");
    	  
    	  break;
      case 3:
    	  break;
      default:
    	  System.out.println("");

      }
      writerop.close();
   
   } 
}
