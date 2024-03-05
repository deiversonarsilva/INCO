package br.com.sankhya.dars.acoes;

import br.com.sankhya.extensions.actionbutton.AcaoRotinaJava;
import br.com.sankhya.extensions.actionbutton.ContextoAcao;
import br.com.sankhya.extensions.actionbutton.Registro;
import br.com.sankhya.jape.dao.JdbcWrapper;
import br.com.sankhya.jape.wrapper.JapeFactory;
import br.com.sankhya.jape.wrapper.JapeWrapper;
import br.com.sankhya.modelcore.util.EntityFacadeFactory;

import java.math.BigDecimal;

public class acaoExcluiApoItem implements AcaoRotinaJava {
    @Override
    public void doAction(ContextoAcao contextoAcao) throws Exception {
        JdbcWrapper jdbc = EntityFacadeFactory.getDWFFacade().getJdbcWrapper();
        jdbc.openSession();

        JapeWrapper iteDAO = JapeFactory.dao("ApontamentoMateriais");//TPRAMP
        Registro[] linhas = contextoAcao.getLinhas();
        if (contextoAcao.confirmarSimNao("Confirma a exclusão", "Confirma a exclusão dos itens selecionados ?", 1)) {
            for (Registro linha : linhas) {
                BigDecimal nuapo = (BigDecimal) linha.getCampo("NUAPO");
                BigDecimal seqmp = (BigDecimal) linha.getCampo("SEQMP");
                BigDecimal codprodmp = (BigDecimal) linha.getCampo("CODPRODMP");
                iteDAO.deleteByCriteria("NUAPO=? AND CODPRODMP=? AND SEQMP=?", nuapo, codprodmp, seqmp);
                }
        }
        contextoAcao.setMensagemRetorno("Item Excluido!!!");
        jdbc.closeSession();
    }

}
