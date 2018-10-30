import java.util.*;
import redis.clients.jedis.Jedis; 

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

		//Variaveis aposta
		String rodada;
		String partida;
		String resultadoApostado;
		String strConc;
		List<String> Resultados ;


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
					op = writer.nextInt();
					switch(op) {
					case 1:
						String continuar = "S";
						while (continuar == "S") {
							writer = new Scanner(System.in);
							System.out.println("INSIRA A RODADA (1-38)");
							rodada = writer.nextLine();
							System.out.println("INSIRA A PARTIDA (1-10)");
							partida = writer.nextLine();
							System.out.println("INSIRA SUA APOSTA (A - B - E)");
							resultadoApostado = writer.nextLine();

							strConc = rodada + "_" + partida + "_" + resultadoApostado;
							jedis.sadd("APOSTAS:"+apelido,strConc);
							
							if (jedis.smembers()) {
								
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
					}


				}else {
					System.out.println("---CADASTRO USUARIO---");

					System.out.println("Insira seu apelido:");
					apelido = writer.nextLine();
					jedis.hset("USUARIO:"+apelido," APELIDO ", apelido);

					System.out.println("Insira seu nome completo:");
					nome = writer.nextLine();
					jedis.hset("USUARIO:"+apelido," NOME ", nome);

					System.out.println("Insira sua data de nascimento:");
					datanasc = writer.nextLine();
					jedis.hset("USUARIO:"+apelido," DATA_NASCIMENTO ", datanasc);

					System.out.println("Insira seu genero(M/F):");
					genero = writer.nextLine();
					jedis.hset("USUARIO:"+apelido," GENERO ", genero);

					System.out.println("Insira seu pais:");
					pais = writer.nextLine();
					jedis.hset("USUARIO:"+apelido," PAIS ", pais);

					System.out.println("Insira seu estado:");
					estado = writer.nextLine();
					jedis.hset("USUARIO:"+apelido," ESTADO ", estado);

					System.out.println("Insira o nome da sua cidade:");
					cidade = writer.nextLine();
					jedis.hset("USUARIO:"+apelido," CIDADE ", cidade);

					System.out.println("Insira o nome da sua rua:");
					rua = writer.nextLine();
					jedis.hset("USUARIO:"+apelido," RUA ", rua);

					System.out.println("Insira o complemento:");
					complemento = writer.nextLine();
					jedis.hset("USUARIO:"+apelido," COMPLEMENTO ", complemento);

					System.out.println("Insira o c�digo postal:");
					codigopostal = writer.nextLine();
					jedis.hset("USUARIO:"+apelido," CODIGO_POSTAL ", codigopostal);
					break;
				}

			case 2:
				jedis.flushAll();
				String alf = "ABE";
				int N = alf.length();
				int[] rodadas;
				char c;
				rodadas = new int[38];
				for (int i = 0; i < rodadas.length; i++) {
					jedis.hset("RODADA:IDRODADA:"+i, "IDRODADA",Integer.toString(i));
					for (int j = 0; j < 10; j++) {
						Random r = new Random();
						c = alf.charAt(r.nextInt(N));
						String s = Character.toString(c);
						jedis.zadd("RODADA:IDRODADA:"+i+":RESULTADO", 0, Integer.toString(c));
						strConc = i + "_" + j + "_" + s;
					}
				}
				break;
				default:
					System.out.println("opcao invalida");
			}
		}
	}
}








