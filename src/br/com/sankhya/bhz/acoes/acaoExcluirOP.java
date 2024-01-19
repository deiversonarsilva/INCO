package br.com.sankhya.bhz.acoes;

import br.com.sankhya.extensions.actionbutton.AcaoRotinaJava;
import br.com.sankhya.extensions.actionbutton.ContextoAcao;
import br.com.sankhya.extensions.actionbutton.Registro;
import br.com.sankhya.jape.dao.JdbcWrapper;
import br.com.sankhya.jape.wrapper.JapeFactory;
import br.com.sankhya.jape.wrapper.JapeWrapper;
import br.com.sankhya.modelcore.util.EntityFacadeFactory;

import java.math.BigDecimal;

public class acaoExcluirOP implements AcaoRotinaJava {
    @Override
    public void doAction(ContextoAcao contextoAcao) throws Exception {
        JdbcWrapper jdbc = EntityFacadeFactory.getDWFFacade().getJdbcWrapper();
        jdbc.openSession();
        JapeWrapper idiDAO = JapeFactory.dao("CabecalhoInstanciaProcesso");//TPRIPROC
        JapeWrapper ipaDAO = JapeFactory.dao("ProdutoAcabadoAProduzir");//TPRIPA
        JapeWrapper idepDAO = JapeFactory.dao("DependenciaEntreOps");//TPRIDEP
        JapeWrapper esrDAO = JapeFactory.dao("EstoqueRepositorioPA");//TPRESR
        JapeWrapper merDAO = JapeFactory.dao("MovimentacaoRepositorioPA");//TPRMER
        BigDecimal idiproc = BigDecimal.ZERO;
        Registro[] linhas = contextoAcao.getLinhas();
        for (Registro linha : linhas) {
            idiproc = (BigDecimal) linha.getCampo("IDIPROC");
            merDAO.deleteByCriteria("IDIPROC=?",idiproc);
            esrDAO.deleteByCriteria("IDIPROC=?",idiproc);
            idepDAO.deleteByCriteria("IDIPROCPA=? ",idiproc);
            ipaDAO.deleteByCriteria("IDIPROC=?",idiproc);
            idiDAO.deleteByCriteria("IDIPROC=?",idiproc);
        }
        jdbc.closeSession();
        contextoAcao.setMensagemRetorno("Processo Finalizado!!!");
    }
}
