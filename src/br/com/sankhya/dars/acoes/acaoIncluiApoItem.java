package br.com.sankhya.dars.acoes;

import br.com.sankhya.extensions.actionbutton.AcaoRotinaJava;
import br.com.sankhya.extensions.actionbutton.ContextoAcao;
import br.com.sankhya.extensions.actionbutton.Registro;
import br.com.sankhya.jape.dao.JdbcWrapper;
import br.com.sankhya.jape.wrapper.JapeFactory;
import br.com.sankhya.jape.wrapper.JapeWrapper;
import br.com.sankhya.modelcore.util.EntityFacadeFactory;

import java.math.BigDecimal;

public class acaoIncluiApoItem implements AcaoRotinaJava {
    @Override
    public void doAction(ContextoAcao contextoAcao) throws Exception {
        JdbcWrapper jdbc = EntityFacadeFactory.getDWFFacade().getJdbcWrapper();
        jdbc.openSession();

        JapeWrapper iteDAO = JapeFactory.dao("ApontamentoMateriais");//TPRAMP
        JapeWrapper apoDAO = JapeFactory.dao("ApontamentoPA");//TPRAPA
        JapeWrapper proDAO = JapeFactory.dao("Produto");//TGFPRO
        Registro[] linhas = contextoAcao.getLinhas();
            for (Registro linha : linhas) {
                BigDecimal nuapo = (BigDecimal) linha.getCampo("NUAPO");
                BigDecimal seqapa = (BigDecimal) linha.getCampo("SEQAPA");
                BigDecimal codprodmp = (BigDecimal) contextoAcao.getParam("CODPRODMP");
                String controle = (String) contextoAcao.getParam("CONTROLE");
                BigDecimal qtd = (BigDecimal) contextoAcao.getParam("QTD");
                String codvol = proDAO.findOne("CODPROD=?",codprodmp).asString("CODVOL");
                BigDecimal codlocalbaixa = proDAO.findOne("CODPROD=?",codprodmp).asBigDecimalOrZero("CODLOCALPADRAO");
                iteDAO.create()
                        .set("NUAPO",nuapo)
                        .set("SEQAPA",seqapa)
                        .set("CODPRODMP",codprodmp)
                        .set("CONTROLEMP",controle)
                        .set("QTD",qtd)
                        .set("CODVOL",codvol)
                        .set("TIPOUSO","C")
                        .set("VINCULOSERIEPA","N")
                        .set("SEQMP",iteDAO.findOne("SEQMP=(SELECT MAX(SEQMP) FROM TPRAMP WHERE NUAPO=?)").asBigDecimalOrZero("SEQMP").add(BigDecimal.ONE))
                        .set("CODLOCALBAIXA",codlocalbaixa)
                        .save();
            }

        contextoAcao.setMensagemRetorno("Item Excluido!!!");
        jdbc.closeSession();
    }

}

