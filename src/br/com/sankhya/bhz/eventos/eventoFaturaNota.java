package br.com.sankhya.bhz.eventos;

import br.com.sankhya.extensions.eventoprogramavel.EventoProgramavelJava;
import br.com.sankhya.jape.event.PersistenceEvent;
import br.com.sankhya.jape.event.TransactionContext;
import br.com.sankhya.jape.vo.DynamicVO;
import br.com.sankhya.jape.wrapper.JapeFactory;
import br.com.sankhya.jape.wrapper.JapeWrapper;
import br.com.sankhya.bhz.utils.Utilitarios;

import java.math.BigDecimal;
import java.util.Collection;

public class eventoFaturaNota implements EventoProgramavelJava { //TGFCAB
    JapeWrapper cabDAO = JapeFactory.dao("CabecalhoNota");
    JapeWrapper iteDAO = JapeFactory.dao("ItemNota");
    JapeWrapper topDAO = JapeFactory.dao("TipoOperacao");
    public void atualizaFaturamento(BigDecimal nunota) throws Exception{
        DynamicVO notaVO = cabDAO.findByPK(nunota);
        DynamicVO topVO = topDAO.findOne("CODTIPOPER=? AND DHALTER=?",notaVO.asBigDecimalOrZero("CODTIPOPER"),notaVO.asTimestamp("DHTIPOPER"));
        BigDecimal percentual = topVO.asBigDecimalOrZero("AD_PERCFATUR");
        Boolean ativado = "S".equals(topVO.asString("AD_ATUALFATUR"));
        if(ativado){
            Collection<DynamicVO> iteVO = iteDAO.find("NUNOTA=?",nunota);
            BigDecimal vlrunit = BigDecimal.ZERO;
            BigDecimal vlrtot = BigDecimal.ZERO;
            for(DynamicVO ite : iteVO){
                vlrunit = (ite.asBigDecimalOrZero("VLRUNIT").multiply(percentual)).divide(new BigDecimal(100));
                vlrtot = vlrunit.multiply(ite.asBigDecimalOrZero("QTDNEG"));
                iteDAO.prepareToUpdate(ite)
                        .set("VLRUNIT",vlrunit)
                        .set("VLRTOT",vlrtot)
                        .update();
            }
            Utilitarios.recalculaImpostosNota(nunota);
        }
    }
    @Override
    public void beforeInsert(PersistenceEvent persistenceEvent) throws Exception {

    }

    @Override
    public void beforeUpdate(PersistenceEvent persistenceEvent) throws Exception {

    }

    @Override
    public void beforeDelete(PersistenceEvent persistenceEvent) throws Exception {

    }

    @Override
    public void afterInsert(PersistenceEvent persistenceEvent) throws Exception {
        DynamicVO vo = (DynamicVO) persistenceEvent.getVo();
        BigDecimal nunota = vo.asBigDecimalOrZero("NUNOTA");
        atualizaFaturamento(nunota);
    }

    @Override
    public void afterUpdate(PersistenceEvent persistenceEvent) throws Exception {

    }

    @Override
    public void afterDelete(PersistenceEvent persistenceEvent) throws Exception {

    }

    @Override
    public void beforeCommit(TransactionContext transactionContext) throws Exception {

    }
}
