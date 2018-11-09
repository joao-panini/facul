import java.util.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple; 

public class RedisJava { 
	@SuppressWarnings("resource")
	public static void main(String[] args) { 
		//Conectando no Redis server -  localhost 
		Jedis jedis = new Jedis("localhost"); 
		System.out.println("Connec√ß√£o com o servidor"); 
		//Testando se o servidor est√° execuando 
		System.out.println("Servidor est√° executando: "+jedis.ping()); 



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
		double auxiliar = 0;
		int contador = 1;
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
							
							String resultadoPartidaApostada = jedis.hget("RODADA:"+rodada+":PARTIDA:"+partida, "RESULTADO");
							
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
						continuar = "S";
						while (continuar.equalsIgnoreCase("S")) {
							writer = new Scanner(System.in);
							System.out.println("\nINSIRA O USUARIO QUE DESEJA LISTAR:");
							apelido = writer.nextLine();
	
							Set<String> setC = jedis.smembers("APOSTAS:"+apelido);
							for(String s : setC) {
								String[] sCorrigida = s.split("_");
								System.out.println("Rodada apostada:"+ sCorrigida[0]);
								System.out.println("Partida apostada:"+ sCorrigida[1]);
								System.out.println("Resultado apostado:"+ sCorrigida[2]);
								System.out.println("\n");
							}
							
							System.out.println("Deseja realizar outra consulta? (S/N)");
							continuar = writer.nextLine().toUpperCase();
						}
						break;
					case 3:
						writer = new Scanner(System.in);
						Usuarios = jedis.zrange("APELIDO", 0, -1);
						pontuacao = 0;
						
						for (String user : Usuarios) {
							pontuacao = Integer.parseInt(jedis.hget("USUARIO:"+user, "SCORE"));
							jedis.zadd("RANKING",pontuacao,user);
						}
						
						Ranking = jedis.zrevrangeWithScores("RANKING", 0, -1);
						System.out.println("--- RANKING ---\n");
						for (Tuple rtuple : Ranking) {
							if(rtuple.getScore() != auxiliar) {
								System.out.println("PosiÁ„o: " + contador);
								System.out.println("Pontos: " + rtuple.getScore());
								System.out.println("Apelido: "+ rtuple.getElement() + "\n");
								contador++;
							}
						}
						
					}
					break;


				}else {
					System.out.println("USUARIO N√O CADASTRADO! \n \n");
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

					System.out.println("Insira o cÛdigo postal:");
					codigopostal = writer.nextLine();
					jedis.hset("USUARIO:"+apelido,"CODIGO_POSTAL", codigopostal);
					
					jedis.hset("USUARIO:"+apelido, "SCORE" , "0");
					
					System.out.println("USUARIO CADASTRADO COM SUCESSO!!");
					break;
				}

			case 2:
				jedis.flushAll();
				String alf = "ABE";
				int N = alf.length();
				int[] rodadas;
				char c;
				rodadas = new int[38];
				
				jedis.zadd("APELIDO",1,"JOAO");
				jedis.zadd("APELIDO",1,"PROFESSOR");
				jedis.zadd("APELIDO",1,"ALUNO");
				jedis.zadd("APELIDO",1,"TESTE");
				
				jedis.hset("USUARIO:JOAO","APELIDO", "JOAO");
				jedis.hset("USUARIO:PROFESSOR","APELIDO", "PROFESSOR");
				jedis.hset("USUARIO:ALUNO","APELIDO", "ALUNO");
				jedis.hset("USUARIO:TESTE","APELIDO", "TESTE");
				
				jedis.hset("USUARIO:JOAO", "SCORE" , "0");
				jedis.hset("USUARIO:PROFESSOR", "SCORE" , "0");
				jedis.hset("USUARIO:ALUNO", "SCORE" , "0");
				jedis.hset("USUARIO:TESTE", "SCORE" , "0");
				
				for (int i = 1; i <= 38; i++) {
					for (int j = 1; j <= 10; j++) {
						//RESULTADOS FAKE DAS PARTIADS
						Random r = new Random();
						c = alf.charAt(r.nextInt(N));
						String s = Character.toString(c);
						jedis.hset("RODADA:"+i+":PARTIDA:"+j, "RESULTADO", s);
						String result = jedis.hget("RODADA:"+i+":PARTIDA:"+j, "RESULTADO");
						System.out.println("Rodada " + i + " Partida " + j + " Resultado " + result);
						
						//APOSTAS FAKE COM VALIDACAO E SCORE
						c = alf.charAt(r.nextInt(N));
						s = Character.toString(c);
						jedis.sadd("APOSTAS:JOAO", i + "_" + j + "_" + s);						
						if (result.equals(s)) {
							pontuacao = 0;
							pontuacao = Integer.parseInt(jedis.hget("USUARIO:JOAO", "SCORE"));
							pontuacao++;
							jedis.hset("USUARIO:JOAO", "SCORE", Integer.toString(pontuacao));
						}
						
						c = alf.charAt(r.nextInt(N));
						s = Character.toString(c);
						jedis.sadd("APOSTAS:PROFESSOR", i + "_" + j + "_" + s);						
						if (result.equals(s)) {
							pontuacao = 0;
							pontuacao = Integer.parseInt(jedis.hget("USUARIO:PROFESSOR", "SCORE"));
							pontuacao++;
							jedis.hset("USUARIO:PROFESSOR", "SCORE", Integer.toString(pontuacao));
						}
						
						c = alf.charAt(r.nextInt(N));
						s = Character.toString(c);
						jedis.sadd("APOSTAS:ALUNO", i + "_" + j + "_" + s);						
						if (result.equals(s)) {
							pontuacao = 0;
							pontuacao = Integer.parseInt(jedis.hget("USUARIO:ALUNO", "SCORE"));
							pontuacao++;
							jedis.hset("USUARIO:ALUNO", "SCORE", Integer.toString(pontuacao));
						}
						
						c = alf.charAt(r.nextInt(N));
						s = Character.toString(c);
						jedis.sadd("APOSTAS:TESTE", i + "_" + j + "_" + s);						
						if (result.equals(s)) {
							pontuacao = 0;
							pontuacao = Integer.parseInt(jedis.hget("USUARIO:TESTE", "SCORE"));
							pontuacao++;
							jedis.hset("USUARIO:TESTE", "SCORE", Integer.toString(pontuacao));
						}
						
					}
				}


				break;
				default:
					System.out.println("opcao invalida");
			}
		}
	}
}








