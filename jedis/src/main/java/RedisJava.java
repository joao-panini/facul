import java.util.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple; 

public class RedisJava { 
	@SuppressWarnings("resource")
	public static void main(String[] args) { 
		//Conectando no Redis server -  localhost 
		Jedis jedis = new Jedis("localhost"); 
		System.out.println("Connecção com o servidor"); 
		//Testando se o servidor está execuando 
		System.out.println("Servidor está executando: "+jedis.ping()); 



		Scanner writer = new Scanner(System.in);
		int op = 1;

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
		String listmemb;
		int pontuacao;

		//Variaveis aposta
		String rodada;
		String partida;
		String resultadoApostado;
		String strConc;
		String resultadofoda;
		List<String> Resultados;
		Set<String> Usuarios;
		Set<Tuple> Ranking;


		while (op != 0) {

			System.out.println("---- MENU ----");
			System.out.println("1 - LOGAR");
			System.out.println("2 - REGISTRAR DADOS FAKE");
			op = writer.nextInt();


			switch(op) {
			case 1:
				writer = new Scanner(System.in);
				System.out.println("Insira seu apelido");
				apelido = writer.nextLine();

				if(jedis.zadd("APELIDO",1,apelido) == 0) {
					writer = new Scanner(System.in);

					System.out.println("1 - APOSTAR");
					System.out.println("2 - LISTAR APOSTAS POR USUARIO");
					System.out.println("3 - RANKING");
					op = writer.nextInt();
					switch(op) {
					case 1:
						String continuar = "S";
						while (continuar.equalsIgnoreCase("S")) {
							writer = new Scanner(System.in);
							System.out.println("INSIRA A RODADA (1-38)");
							rodada = writer.nextLine();
							System.out.println("INSIRA A PARTIDA (1-10)");
							partida = writer.nextLine();
							System.out.println("INSIRA SUA APOSTA (A - B - E)");
							resultadoApostado = writer.nextLine();

							strConc = rodada + "_" + partida + "_" + resultadoApostado;
							jedis.sadd("APOSTAS:"+apelido,strConc);
							
							String resultadoPartidaApostada = jedis.hget("rodada:"+rodada+":partida:"+partida, "resultado");
							
							if (resultadoPartidaApostada.equals(resultadoApostado)) {
								pontuacao = Integer.parseInt(jedis.hget("USUARIO:"+apelido, "SCORE"));
								pontuacao++;
								jedis.hset("USUARIO:"+apelido, "SCORE", Integer.toString(pontuacao));
								System.out.println("ACERTOU!!");
							}else {
								System.out.println("ERROU!!");
							}
							

							System.out.println("Aposta concluida!");
							System.out.println("Deseja realizar outra aposta? (S/N)");
							continuar = writer.nextLine().toUpperCase();
						}
						break;
					case 2:
						writer = new Scanner(System.in);
						System.out.println("insira o usuario que deseja listar:");
						apelido = writer.nextLine();

						Set<String> setC = jedis.smembers("APOSTAS:"+apelido);
						for(String s : setC) {
							String[] sCorrigida = s.split("_");
							System.out.println("Rodada apostada:"+ sCorrigida[0]);
							System.out.println("Partida apostada:"+ sCorrigida[1]);
							System.out.println("Resultado apostado:"+ sCorrigida[2]);
							System.out.println("\n");
						}
						break;
					case 3:
						writer = new Scanner(System.in);
						Usuarios = jedis.zrange("APELIDO", 0, -1);
						pontuacao = 0;
						
						for (String user : Usuarios) {
							pontuacao = Integer.parseInt(jedis.hget("USUARIO:"+apelido, "SCORE"));
							jedis.zadd("RANKING",pontuacao,user);
						}
						
						Ranking = jedis.zrangeWithScores("RANKING", 0, -1);
						System.out.println("--- RANKING ---\n");
						for (Tuple rtuple : Ranking) {
							System.out.println("Apelido: "+ rtuple.getElement());
							System.out.println("Pontos: "+ rtuple.getScore()+ "\n");
						}
						
						
						
					}


				}else {
					System.out.println("---CADASTRO USUARIO---");

					System.out.println("Insira seu apelido:");
					apelido = writer.nextLine();
					jedis.hset("USUARIO:"+apelido,"APELIDO", apelido);
					
					System.out.println("Insira seu nome completo:");
					nome = writer.nextLine();
					jedis.hset("USUARIO:"+apelido,"NOME", nome);

					System.out.println("Insira sua data de nascimento:");
					datanasc = writer.nextLine();
					jedis.hset("USUARIO:"+apelido,"DATA_NASCIMENTO", datanasc);

					System.out.println("Insira seu genero(M/F):");
					genero = writer.nextLine();
					jedis.hset("USUARIO:"+apelido,"GENERO", genero);

					System.out.println("Insira seu pais:");
					pais = writer.nextLine();
					jedis.hset("USUARIO:"+apelido,"PAIS", pais);

					System.out.println("Insira seu estado:");
					estado = writer.nextLine();
					jedis.hset("USUARIO:"+apelido,"ESTADO", estado);

					System.out.println("Insira o nome da sua cidade:");
					cidade = writer.nextLine();
					jedis.hset("USUARIO:"+apelido,"CIDADE", cidade);

					System.out.println("Insira o nome da sua rua:");
					rua = writer.nextLine();
					jedis.hset("USUARIO:"+apelido,"RUA", rua);

					System.out.println("Insira o complemento:");
					complemento = writer.nextLine();
					jedis.hset("USUARIO:"+apelido,"COMPLEMENTO", complemento);

					System.out.println("Insira o c�digo postal:");
					codigopostal = writer.nextLine();
					jedis.hset("USUARIO:"+apelido,"CODIGO_POSTAL", codigopostal);
					
					jedis.hset("USUARIO:"+apelido, "SCORE" , "0");
					break;
				}

			case 2:
				jedis.flushAll();
				String alf = "ABE";
				int N = alf.length();
				int[] rodadas;
				char c;
				rodadas = new int[38];
				for (int i = 1; i <= 38; i++) {
					for (int j = 1; j <= 10; j++) {
						Random r = new Random();
						c = alf.charAt(r.nextInt(N));
						String s = Character.toString(c);
						jedis.hset("rodada:"+i+":partida:"+j, "resultado", s);
						String paraprintar = jedis.hget("rodada:"+i+":partida:"+j, "resultado");
						System.out.println("Rodada " + i + " Partida " + j + " Resultado " + paraprintar);
					}
				}
				jedis.zadd("APELIDO",1,"JOAO");
				jedis.zadd("APELIDO",1,"PROFESSOR");
				jedis.zadd("APELIDO",1,"ALUNO");
				jedis.zadd("APELIDO",1,"TESTE");
				
				
				jedis.sadd("APOSTAS:JOAO","1_1_B");
				jedis.sadd("APOSTAS:JOAO","2_1_A");
				jedis.sadd("APOSTAS:JOAO","3_1_B");
				jedis.sadd("APOSTAS:JOAO","4_1_E");
				jedis.sadd("APOSTAS:JOAO","5_1_B");
				jedis.sadd("APOSTAS:JOAO","6_1_A");
				jedis.sadd("APOSTAS:JOAO","7_1_B");
				jedis.sadd("APOSTAS:JOAO","8_1_E");
				jedis.sadd("APOSTAS:JOAO","9_1_B");
				jedis.sadd("APOSTAS:JOAO","10_1_A");
				
				jedis.sadd("APOSTAS:PROFESSOR","1_2_B");
				jedis.sadd("APOSTAS:PROFESSOR","2_2_A");
				jedis.sadd("APOSTAS:PROFESSOR","3_2_B");
				jedis.sadd("APOSTAS:PROFESSOR","4_2_E");
				jedis.sadd("APOSTAS:PROFESSOR","5_2_B");
				jedis.sadd("APOSTAS:PROFESSOR","6_2_A");
				jedis.sadd("APOSTAS:PROFESSOR","7_2_B");
				jedis.sadd("APOSTAS:PROFESSOR","8_2_E");
				jedis.sadd("APOSTAS:PROFESSOR","9_2_B");
				jedis.sadd("APOSTAS:PROFESSOR","10_2_A");
				
				jedis.sadd("APOSTAS:ALUNO","");
				
				
				
				
				break;
			case 3:
				jedis.hset("rodada:1:partida:1", "resultado", "a");
				resultadofoda =jedis.hget("rodada:1:partida:1", "resultado");
				break;
				
				default:
					System.out.println("opcao invalida");
			}
		}
	}
}








