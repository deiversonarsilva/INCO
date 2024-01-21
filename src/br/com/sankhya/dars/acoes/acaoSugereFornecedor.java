package br.com.sankhya.dars.acoes;

import br.com.sankhya.extensions.actionbutton.AcaoRotinaJava;
import br.com.sankhya.extensions.actionbutton.ContextoAcao;
import br.com.sankhya.extensions.actionbutton.Registro;
import br.com.sankhya.jape.dao.JdbcWrapper;
import br.com.sankhya.jape.vo.DynamicVO;
import br.com.sankhya.jape.wrapper.JapeFactory;
import br.com.sankhya.jape.wrapper.JapeWrapper;
import br.com.sankhya.modelcore.util.EntityFacadeFactory;

import java.math.BigDecimal;
import java.util.Collection;

public class acaoSugereFornecedor implements AcaoRotinaJava {//TGFITC
    @Override
    public void doAction(ContextoAcao contextoAcao) throws Exception {
        JdbcWrapper jdbc = EntityFacadeFactory.getDWFFacade().getJdbcWrapper();
        JapeWrapper papDAO = JapeFactory.dao("RelacionamentoParceiroProduto");
        JapeWrapper forDAO = JapeFactory.dao("ColetaItemCotacao");
        JapeWrapper proDAO = JapeFactory.dao("Produto");
        jdbc.openSession();
        Registro[] linhas = contextoAcao.getLinhas();
        for (Registro linha : linhas) {
            BigDecimal numcotacao = (BigDecimal) linha.getCampo("NUMCOTACAO");
            BigDecimal codprod = (BigDecimal) linha.getCampo("CODPROD");
            DynamicVO prodVO = proDAO.findOne("CODPROD=?",codprod);
            String codvol = prodVO.asString("CODVOL");
            Collection<DynamicVO> papVO = papDAO.find("CODPROD=?",codprod);
            for(DynamicVO pap : papVO) {

                forDAO.create()
                        .set("NUMCOTACAO", numcotacao)
                        .set("CODPROD", codprod)
                        .set("CODPARC",pap.asBigDecimal("CODPARC"))
                        .set("CODVOL",codvol)
                        .set("CODLOCAL",linha.getCampo("CODLOCAL"))
                        .set("QTDCOTADA",linha.getCampo("QTDCOTADA"))
                        .set("TIPOCOLPRECO","MANUAL")
                        .save();
            }

        }
        jdbc.closeSession();
        contextoAcao.setMensagemRetorno("Processo Finalizado!!!");
    }
}
