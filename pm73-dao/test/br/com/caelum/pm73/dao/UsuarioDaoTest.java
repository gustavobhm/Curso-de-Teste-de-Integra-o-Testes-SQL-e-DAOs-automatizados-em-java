package br.com.caelum.pm73.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.pm73.dominio.Usuario;

public class UsuarioDaoTest {

	private Session session;
	private UsuarioDao usuarioDao;

	@Before
	public void antes() {
		// criamos a sessao e a passamos para o dao
		session = new CriadorDeSessao().getSession();
		usuarioDao = new UsuarioDao(session);
	}

	@After
	public void depois() {
		// fechamos a sessao
		session.close();
	}

	@Test
	public void deveEncontrarPeloNomeEEmail() {
		Usuario novoUsuario = new Usuario("Jo�o da Silva", "joao@dasilva.com.br");
		usuarioDao.salvar(novoUsuario);

		Usuario usuarioDoBanco = usuarioDao.porNomeEEmail("Jo�o da Silva", "joao@dasilva.com.br");

		assertEquals("Jo�o da Silva", usuarioDoBanco.getNome());
		assertEquals("joao@dasilva.com.br", usuarioDoBanco.getEmail());
	}

	@Test
	public void deveRetornarNuloSeNaoEncontrarUsuario() {
		Usuario usuarioDoBanco = usuarioDao.porNomeEEmail("Jo�o Joaquim", "joao@joaquim.com.br");

		assertNull(usuarioDoBanco);
	}

	@Test
	public void deveDeletarUmUsuario() {
		Usuario usuario = new Usuario("Mauricio Aniche", "mauricio@aniche.com.br");

		usuarioDao.salvar(usuario);
		usuarioDao.deletar(usuario);

		// envia tudo para o banco de dados
		session.flush();
		session.clear();

		Usuario usuarioNoBanco = usuarioDao.porNomeEEmail("Mauricio Aniche", "mauricio@aniche.com.br");

		assertNull(usuarioNoBanco);

	}

	@Test
	public void deveAlterarUmUsuario() {
		Usuario usuario = new Usuario("Mauricio Aniche", "mauricio@aniche.com.br");

		usuarioDao.salvar(usuario);

		usuario.setNome("Jo�o da Silva");
		usuario.setEmail("joao@silva.com.br");

		usuarioDao.atualizar(usuario);

		session.flush();

		Usuario novoUsuario = usuarioDao.porNomeEEmail("Jo�o da Silva", "joao@silva.com.br");
		assertNotNull(novoUsuario);
		System.out.println(novoUsuario);

		Usuario usuarioInexistente = usuarioDao.porNomeEEmail("Mauricio Aniche", "mauricio@aniche.com.br");
		assertNull(usuarioInexistente);

	}

}
