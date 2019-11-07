package automation;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import dao.ConfiguracoesJogoDAO;
import dao.ItemDAO;
import dao.MusicaDAO;
import dao.PerguntaMultiplaEscolhaDAO;
import dao.PerguntaRelacionarDAO;
import dao.PerguntaVerdadeiroFalsoDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.ConfiguracoesJogo;
import model.Item;
import model.Musica;
import model.PerguntaMultiplaEscolha;
import model.PerguntaRelacionar;
import model.PerguntaVerdadeiroFalso;

public class InicializadorBanco {
	public InicializadorBanco () {
		
	}

	public void inicializaBanco() {
		PerguntaMultiplaEscolhaDAO perguntaMultiplaEscolhaDAO = new PerguntaMultiplaEscolhaDAO();
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("objectdb/db/GameDataBase.odb");
		EntityManager em = emf.createEntityManager();
		
		ObservableList<PerguntaMultiplaEscolha> list = FXCollections.observableArrayList(perguntaMultiplaEscolhaDAO.getListPerguntasMultiplaEscolha(em));
		
		if (!list.isEmpty()){
			System.out.println("O banco de perguntas de multipla escolha já foi criado e populado");
		} else {
			System.out.println("Criando e populando banco de perguntas de multipla escolha");
			
			int perguntaIterator = 1;
			int qtdPerguntas = 5;
			
			String descricao = null;
			String fonte = null;
			int alternativaCorreta = 0;
			String alternativa1 = null;
			String alternativa2 = null;
			String alternativa3 = null;
			String alternativa4 = null;
			List<String> listAlternativas;
			
			for (perguntaIterator = 1; perguntaIterator <= qtdPerguntas; perguntaIterator++) {
				switch (perguntaIterator) {
					case 1 : {
						descricao = "Representa um relato de uso de certa funcionalidade do sistema em questão, "
								+ "sem revelar a estrutura e o comportamento interno desse sistema.";
						fonte = "Bezerra, E., 2011. P. 54.";
						alternativaCorreta = 2;
						
						alternativa1 = "Diagrama de Classe";
						alternativa2 = "Diagrama de Caso de Uso";
						alternativa3 = "Diagrama de Atividade";
						alternativa4 = "Diagrama de Sequência";
						
						break;
					}
					case 2 :{
						descricao = "O Modelo de Caso de Uso representa os possíveis usos de um sistema, "
								+ "conforme percebidos por um observador externo a este sistema. O que está associado a cada um desses usos "
								+ "do sistema?";
						fonte = "Bezerra, E., 2011. P. 54.";
						alternativaCorreta = 3;
						
						alternativa1 = "Regras de Negócio";
						alternativa2 = "Requisitos Não-Funcionais";
						alternativa3 = "Requisitos Funcionais";
						alternativa4 = "Requisitos Organizacionais";
						
						break;
					}
					case 3 :{
						descricao = "É um elemento externo ao sistema que interage com o mesmo";
						fonte = "Bezerra, E., 2011. P. 60.";
						alternativaCorreta = 2;
						
						alternativa1 = "Caso de Uso";
						alternativa2 = "Ator";
						alternativa3 = "Relação";
						alternativa4 = "Cenário";
						
						break;
					}
					case 4 : {
						descricao = "É um elemento responsável por fazer a ligação entre "
								+ "os atores e os casos de uso";
						fonte = "Bezerra, E., 2011. P. 61.";
						alternativaCorreta = 1;
						
						alternativa1 = "Relacionamento";
						alternativa2 = "Dependência";
						alternativa3 = "Herança";
						alternativa4 = "Cenário";
						
						break;
					}
					case 5 : {
						descricao = "Tem a função de realizar uma sequência de interações de "
								+ "um caso de uso";
						fonte = "Bezerra, E., 2011. P. 54.";
						alternativaCorreta = 4;
						
						alternativa1 = "Ator Secundário";
						alternativa2 = "Stakeholder";
						alternativa3 = "Ator Central";
						alternativa4 = "Ator Primário";
						
						break;
					}
					case 6 : {
						descricao = "Tem a função de realizar uma sequência de interações de "
								+ "um caso de uso";
						fonte = "Bezerra, E., 2011. P. 54.";
						alternativaCorreta = 4;
						
						alternativa1 = "Ator Secundário";
						alternativa2 = "Stakeholder";
						alternativa3 = "Ator Central";
						alternativa4 = "Ator Primário";
						
						break;
					}
				}
				
				listAlternativas = new ArrayList<String>();
				listAlternativas.add(alternativa1);
				listAlternativas.add(alternativa2);
				listAlternativas.add(alternativa3);
				listAlternativas.add(alternativa4);
				
				if (descricao != null) {
					EntityManager emPergunta = emf.createEntityManager();
					
					PerguntaMultiplaEscolha pergunta = new PerguntaMultiplaEscolha(descricao, fonte, alternativaCorreta, listAlternativas);
				
					perguntaMultiplaEscolhaDAO.armazenaDados(emPergunta, pergunta);
				}
			}
		}
		
		PerguntaVerdadeiroFalsoDAO perguntaVerdadeiroFalsoDAO = new PerguntaVerdadeiroFalsoDAO();
		EntityManagerFactory emfPerguntasVerdadeiroFalso = Persistence.createEntityManagerFactory("objectdb/db/GameDataBase.odb");
		EntityManager emPerguntasVerdadeiroFalso = emfPerguntasVerdadeiroFalso.createEntityManager();
		
		ObservableList<PerguntaVerdadeiroFalso> listPerguntasVerdadeiroFalso = FXCollections.observableArrayList(perguntaVerdadeiroFalsoDAO.getListPerguntasVerdadeiroFalso(emPerguntasVerdadeiroFalso));
		
		if (!listPerguntasVerdadeiroFalso.isEmpty()){
			System.out.println("O banco de perguntas de verdadeiro/falso já foi criado e populado");
		} else {
			System.out.println("Criando e populando banco de perguntas de verdadeiro/falso");
			
			int perguntaIterator = 1;
			int qtdPerguntas = 3;
			
			String descricao = null;
			String fonte = null;
			int alternativaCorreta = 1;
			String alternativa1 = null;
			String alternativa2 = null;
			List<String> listAlternativas;
			
			for (perguntaIterator = 1; perguntaIterator <= qtdPerguntas; perguntaIterator++) {
				switch (perguntaIterator) {
					case 1 : {
						descricao = "O grau de abstração de um caso de uso diz respeito à existência ou "
								+ "não de menção a aspectos relativos à tecnologia durante a descrição desse caso de uso.";
						fonte = "Bezerra, E., 2011. P. 57.";
						alternativaCorreta = 1;
						
						alternativa1 = "Verdadeiro";
						alternativa2 = "Falso";
						
						break;
					}
					case 2 :{
						descricao = "Uma precondição de um caso de uso define que hipóteses são "
								+ "assumidas como falsas para que o caso de uso tenha início";
						fonte = "Bezerra, E., 2011. P. 81.";
						alternativaCorreta = 2;
						
						alternativa1 = "Verdadeiro";
						alternativa2 = "Falso";
						
						break;
					}
					case 3 :{
						descricao = "O relacionamento de generalização permite que um ator "
								+ "herde características de um ator mais genérico, este último "
								+ "normalmente chamado de ator base";
						fonte = "Bezerra, E., 2011. P. 65.";
						alternativaCorreta = 1;
						
						alternativa1 = "Verdadeiro";
						alternativa2 = "Falso";
						
						break;
					}
				}
				
				listAlternativas = new ArrayList<String>();
				listAlternativas.add(alternativa1);
				listAlternativas.add(alternativa2);
				
				if (descricao != null) {
					EntityManager emPergunta = emfPerguntasVerdadeiroFalso.createEntityManager();
					
					PerguntaVerdadeiroFalso pergunta = new PerguntaVerdadeiroFalso(descricao, fonte, alternativaCorreta, listAlternativas);
				
					perguntaVerdadeiroFalsoDAO.armazenaDados(emPergunta, pergunta);
				}
			}
		}
		
		PerguntaRelacionarDAO perguntaRelacionarDAO = new PerguntaRelacionarDAO();
		EntityManagerFactory emfPerguntasRelacionar = Persistence.createEntityManagerFactory("objectdb/db/GameDataBase.odb");
		EntityManager emPerguntasRelacionar = emfPerguntasRelacionar.createEntityManager();
		
		ObservableList<PerguntaRelacionar> listPerguntasRelacionar = FXCollections.observableArrayList(perguntaRelacionarDAO.getListPerguntasRelacionar(emPerguntasRelacionar));
		
		if (!listPerguntasRelacionar.isEmpty()){
			System.out.println("O banco de perguntas de relacionar já foi criado e populado");
		} else {
			System.out.println("Criando e populando banco de perguntas de relacionar");
			
			int perguntaIterator = 1;
			int qtdPerguntas = 2;
			
			String descricao = null;
			String fonte = null;
			
			String textoAlternativa1 = null;
			String textoAlternativa2 = null;
			String textoAlternativa3 = null;
			String textoAlternativa4 = null;
			List<String> textoAlternativas;
			
			// Neste primeiro momento, ficaram fixas 4 alternativas apenas
			List<Integer> alternativas = new ArrayList<Integer>();
			for (int i = 1; i <= 4; i++) {
				alternativas.add(i);
			}
			
			List<Integer> alternativasOrdemCorreta = new ArrayList<Integer>();
			int alternativa1 = 0;
			int alternativa2 = 0;
			int alternativa3 = 0;
			int alternativa4 = 0;
			
			List<String> textoRespostas = new ArrayList<String>();
			String textoResposta1 = null;
			String textoResposta2 = null;
			String textoResposta3 = null;
			String textoResposta4 = null;
			
			for (perguntaIterator = 1; perguntaIterator <= qtdPerguntas; perguntaIterator++) {
				switch (perguntaIterator) {
					case 1 : {
						descricao = "Relacione o numero de cada alternativa à sua resposta "
								+ "correspondente";
						fonte = "Bezerra, E.";
						
						textoAlternativa1 = "1. Relacionamento de Comunicação";
						textoAlternativa2 = "2. Relacionamento de Inclusão";
						textoAlternativa3 = "3. Relacionamento de Extensão";
						textoAlternativa4 = "4. Generalização";
						
						alternativa1 = 3;
						alternativa2 = 2;
						alternativa3 = 4;
						alternativa4 = 1;
						
						textoResposta1 = "É utilizado para modelar situações em que diferentes "
								+ "interações podem ser inseridas em um mesmo caso de uso.";
						textoResposta2 = "Neste relacionamento, há uma sequência de interações "
								+ "em que mais de um caso de uso pode estar relacionado";
						textoResposta3 = "Permite que um caso de uso herde características "
								+ "de um caso de uso mais genérico.";
						textoResposta4 = "Informa a que caso de uso o ator está associado.";
						
						break;
					}
					case 2 : {
						descricao = "Relacione o numero de cada alternativa à sua resposta "
								+ "correspondente";
						fonte = "Bezerra, E.";
						
						textoAlternativa1 = "1. Diagrama de Classe";
						textoAlternativa2 = "2. Diagrama de Caso de Uso";
						textoAlternativa3 = "3. Diagrama de Sequência";
						textoAlternativa4 = "4. Diagrama de Estado";
						
						alternativa1 = 4;
						alternativa2 = 1;
						alternativa3 = 2;
						alternativa4 = 3;
						
						textoResposta1 = "Mostra os possíveis estados de um objeto e as transações "
								+ "responsáveis pelas suas mudanças de estado.";
						textoResposta2 = "Neste diagrama, podemos representar a existência "
								+ "de relacionamento entre dois objetos.";
						textoResposta3 = "Representa os possíveis usos de um sistema, conforme "
								+ "percebidos por um observador externo a este sistema.";
						textoResposta4 = "Apresenta as interações entre objetos na ordem "
								+ "temporal em que elas acontecem.";
						
						break;
					}
				}
				
				textoAlternativas = new ArrayList<String>();
				textoAlternativas.add(textoAlternativa1);
				textoAlternativas.add(textoAlternativa2);
				textoAlternativas.add(textoAlternativa3);
				textoAlternativas.add(textoAlternativa4);
				
				alternativasOrdemCorreta = new ArrayList<Integer>();
				alternativasOrdemCorreta.add(alternativa1);
				alternativasOrdemCorreta.add(alternativa2);
				alternativasOrdemCorreta.add(alternativa3);
				alternativasOrdemCorreta.add(alternativa4);
				
				textoRespostas = new ArrayList<String>();
				textoRespostas.add(textoResposta1);
				textoRespostas.add(textoResposta2);
				textoRespostas.add(textoResposta3);
				textoRespostas.add(textoResposta4);
				
				if (descricao != null) {
					EntityManager emPergunta = emfPerguntasRelacionar.createEntityManager();
					
					PerguntaRelacionar pergunta = new PerguntaRelacionar(descricao, fonte, textoAlternativas, alternativas, alternativasOrdemCorreta, textoRespostas);
				
					perguntaRelacionarDAO.armazenaDados(emPergunta, pergunta);
				}
			}
		}
		
		ItemDAO itemDAO = new ItemDAO();
		EntityManagerFactory emfItem = Persistence.createEntityManagerFactory("objectdb/db/Loja.odb");
		EntityManager emExisteDatabaseItens = emfItem.createEntityManager();
		
		ObservableList<Item> listItens = FXCollections.observableArrayList(itemDAO.getListItens(emExisteDatabaseItens));
		
		if (!listItens.isEmpty()){
			System.out.println("O banco de itens já foi criado e populado");
		} else {
			System.out.println("Criando e populando banco de itens");
			
			int itemIterator = 1;
			int qtdItens = 2;
			
			String nome = null;
			String descricao = null;
			String imgNome = null;
			int preco = 0;
			
			for (itemIterator = 1; itemIterator <= qtdItens; itemIterator++) {
				switch (itemIterator) {
					// TODO Se mudar o nome da imagem que mudar o nome no AddonsPartidaComponent tbm
					case 1 : {
						nome = "Poção de Vida";
						descricao = "Recupera um coração durante a partida";
						imgNome = "pocaoVida";
						preco = 100;
						
						break;
					}
					case 2 : {
						nome = "Cubo de Gelo";
						descricao = "Congela o tempo por alguns segundos";
						imgNome = "cuboGelo";
						preco = 150;
						
						break;
					}
				}
				
				if (nome != null) {
					EntityManager emItem = emfItem.createEntityManager();
					
					Item item = new Item(nome, descricao, imgNome, preco);
				
					itemDAO.armazenaDados(emItem, item);
				}
			}
		}
		
		MusicaDAO musicaDAO = new MusicaDAO();
		EntityManagerFactory emfMusica = Persistence.createEntityManagerFactory("objectdb/db/Loja.odb");
		EntityManager emExisteDatabaseMusicas = emfMusica.createEntityManager();
		
		ObservableList<Musica> listMusicas = FXCollections.observableArrayList(musicaDAO.getListMusicas(emExisteDatabaseMusicas));
		
		if (!listMusicas.isEmpty()){
			System.out.println("O banco de musicas já foi criado e populado");
		} else {
			System.out.println("Criando e populando banco de musicas");
			
			int itemIterator = 1;
			int qtdItens = 4;
			
			String nome = null;
			String arquivoNome = null;
			String imgNome = null;
			int preco = 0;
			
			for (itemIterator = 1; itemIterator <= qtdItens; itemIterator++) {
				switch (itemIterator) {
					// TODO Se mudar o nome da imagem que mudar o nome no AddonsPartidaComponent tbm
					case 1 : {
						nome = "Netherplace";
						arquivoNome = "netherplace";
						imgNome = "musicPlayer";
						preco = 200;
						
						break;
					}
					case 2 : {
						nome = "Hypnotic Puzzle";
						arquivoNome = "hypnoticPuzzle";
						imgNome = "musicPlayer";
						preco = 250;
						
						break;
					}
					case 3 : {
						nome = "Bit Puzzle";
						arquivoNome = "bitPuzzle";
						imgNome = "musicPlayer";
						preco = 300;
						
						break;
					}
					case 4 : {
						nome = "Car Theft";
						arquivoNome = "carTheft";
						imgNome = "musicPlayer";
						preco = 400;
						
						break;
					}
				}
				
				if (nome != null) {
					EntityManager emMusica = emfMusica.createEntityManager();
					
					Musica musica = new Musica(nome, arquivoNome, imgNome, preco);
				
					musicaDAO.armazenaDados(emMusica, musica);
				}
			}
		}
		
		ConfiguracoesJogoDAO configuracoesJogoDAO = new ConfiguracoesJogoDAO();
		EntityManagerFactory emfConfiguracoesJogo = Persistence.createEntityManagerFactory("objectdb/db/GameDataBase.odb");
		EntityManager emExisteDatabaseeConfiguracoesJogo = emfConfiguracoesJogo.createEntityManager();
		
		ObservableList<ConfiguracoesJogo> listConfiguracoesJogo = FXCollections.observableArrayList(configuracoesJogoDAO.getListConfiguracoesJogo(emExisteDatabaseeConfiguracoesJogo));
		
		if (!listConfiguracoesJogo.isEmpty()){
			System.out.println("O banco de configuracoes do jogo já foi criado e populado");
		} else {
			System.out.println("Criando e populando banco de configuracoes");
			
			String ordem = "sequencial";
			int tempoParaResponderAtivado = 1;
			int tempoMaxParaResponder = 100;
			int qtdVidas = 5;
			
			EntityManager emConfiguracoesJogo = emfConfiguracoesJogo.createEntityManager();
				
			ConfiguracoesJogo configuracoesJogo = new ConfiguracoesJogo(ordem, tempoParaResponderAtivado, tempoMaxParaResponder, qtdVidas);
			
			configuracoesJogoDAO.armazenaDados(emConfiguracoesJogo, configuracoesJogo);
		}
	}
}
