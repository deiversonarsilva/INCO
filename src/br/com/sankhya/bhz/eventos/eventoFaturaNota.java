package br.com.sankhya.bhz.eventos;

import br.com.sankhya.bhz.utils.AcessoBanco;
import br.com.sankhya.extensions.eventoprogramavel.EventoProgramavelJava;
import br.com.sankhya.jape.event.PersistenceEvent;
import br.com.sankhya.jape.event.TransactionContext;
import br.com.sankhya.jape.vo.DynamicVO;
import br.com.sankhya.jape.wrapper.JapeFactory;
import br.com.sankhya.jape.wrapper.JapeWrapper;
import br.com.sankhya.bhz.utils.Utilitarios;
import com.sankhya.util.BigDecimalUtil;

import java.math.BigDecimal;
import java.util.Collection;

public class eventoFaturaNota implements EventoProgramavelJava { //TGFITE
    JapeWrapper cabDAO = JapeFactory.dao("CabecalhoNota");
    JapeWrapper iteDAO = JapeFactory.dao("ItemNota");
    JapeWrapper topDAO = JapeFactory.dao("TipoOperacao");
    public void atualizaFaturamento(BigDecimal nunota, BigDecimal sequencia) throws Exception{
        DynamicVO notaVO = cabDAO.findByPK(nunota);
        DynamicVO topVO = topDAO.findOne("CODTIPOPER=? AND DHALTER=? AND AD_ATUALFATUR='S'",notaVO.asBigDecimalOrZero("CODTIPOPER"),notaVO.asTimestamp("DHTIPOPER"));

        if(topVO != null){
            BigDecimal percentual = BigDecimalUtil.getValueOrZero(topVO.asBigDecimalOrZero("AD_PERCFATUR"));
            DynamicVO iteVO = iteDAO.findOne("NUNOTA=? AND SEQUENCIA=?",nunota,sequencia);
            BigDecimal vlrunit = BigDecimal.ZERO;
            BigDecimal vlrtot = BigDecimal.ZERO;
            BigDecimal preco = BigDecimal.ZERO;
            AcessoBanco acessobanco = new AcessoBanco();
            try {
                acessobanco.openSession();
                preco = acessobanco.findOne("SELECT SNK_PRECO(0,?) AS PRECO FROM DUAL",iteVO.asBigDecimalOrZero("CODPROD")).getBigDecimal("PRECO");
            }finally {
                acessobanco.closeSession();
            }
            vlrunit = (preco.multiply(percentual)).divide(new BigDecimal(100));
            vlrtot = vlrunit.multiply(iteVO.asBigDecimalOrZero("QTDNEG"));
                iteDAO.prepareToUpdate(iteVO)
                        .set("VLRUNIT",vlrunit)
                        .set("VLRTOT",vlrtot)
                        .update();

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
        BigDecimal sequencia = vo.asBigDecimalOrZero("SEQUENCIA");
        DynamicVO cabVO = cabDAO.findByPK(nunota);
        String tipmov = cabVO.asString("TIPMOV");
        if("V".equals(tipmov)) {
            atualizaFaturamento(nunota, sequencia);
        }
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
