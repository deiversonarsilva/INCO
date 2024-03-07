package br.com.sankhya.dars.eventos;

import br.com.sankhya.dars.utils.AcessoBanco;
import br.com.sankhya.dars.utils.Utilitarios;
import br.com.sankhya.extensions.eventoprogramavel.EventoProgramavelJava;
import br.com.sankhya.jape.event.PersistenceEvent;
import br.com.sankhya.jape.event.TransactionContext;
import br.com.sankhya.jape.vo.DynamicVO;
import br.com.sankhya.jape.wrapper.JapeFactory;
import br.com.sankhya.jape.wrapper.JapeWrapper;
import com.sankhya.util.BigDecimalUtil;

import java.math.BigDecimal;

public class eventoAtualizaCabecalhoNota implements EventoProgramavelJava { //TGFCAB
    JapeWrapper cabDAO = JapeFactory.dao("CabecalhoNota");
    JapeWrapper topDAO = JapeFactory.dao("TipoOperacao");

    public void atualizaNota(BigDecimal nunota) throws Exception {
        DynamicVO notaVO = cabDAO.findByPK(nunota);
        DynamicVO topVO = topDAO.findOne("CODTIPOPER=? AND DHALTER=? AND AD_ATUALFATUR='S'", notaVO.asBigDecimalOrZero("CODTIPOPER"), notaVO.asTimestamp("DHTIPOPER"));
        if(topVO != null){
            AcessoBanco acessobanco = new AcessoBanco();

            try{
                acessobanco.openSession();
                acessobanco.update("UPDATE TGFCAB SET VLRFRETE=0, VLRFRETETOTAL=0,ICMSFRETE=0,BASEICMSFRETE=0 WHERE NUNOTA=?",nunota);
            }finally {
                acessobanco.closeSession();
            }
        }
    }

    @Override
    public void beforeInsert(PersistenceEvent persistenceEvent) throws Exception {
        DynamicVO vo = (DynamicVO) persistenceEvent.getVo();
        String tipmov = vo.asString("TIPMOV");
        if("V".equals(tipmov)) {
            vo.setProperty("VLRFRETE", BigDecimal.ZERO);
            vo.setProperty("VLRFRETETOTAL", BigDecimal.ZERO);
            vo.setProperty("ICMSFRETE", BigDecimal.ZERO);
            vo.setProperty("BASEICMSFRETE", BigDecimal.ZERO);
        }
    }

    @Override
    public void beforeUpdate(PersistenceEvent persistenceEvent) throws Exception {
        DynamicVO vo = (DynamicVO) persistenceEvent.getVo();
        String tipmov = vo.asString("TIPMOV");
        if("V".equals(tipmov)) {
            vo.setProperty("VLRFRETE", BigDecimal.ZERO);
            vo.setProperty("VLRFRETETOTAL", BigDecimal.ZERO);
            vo.setProperty("ICMSFRETE", BigDecimal.ZERO);
            vo.setProperty("BASEICMSFRETE", BigDecimal.ZERO);
        }
    }

    @Override
    public void beforeDelete(PersistenceEvent persistenceEvent) throws Exception {

    }

    @Override
    public void afterInsert(PersistenceEvent persistenceEvent) throws Exception {
        DynamicVO vo = (DynamicVO) persistenceEvent.getVo();
        BigDecimal nunota = vo.asBigDecimalOrZero("NUNOTA");
        DynamicVO cabVO = cabDAO.findByPK(nunota);
        String tipmov = cabVO.asString("TIPMOV");
        if("V".equals(tipmov)) {
            atualizaNota(nunota);
            Utilitarios.recalculaImpostosNota(nunota);

        }
    }

    @Override
    public void afterUpdate(PersistenceEvent persistenceEvent) throws Exception {
        DynamicVO vo = (DynamicVO) persistenceEvent.getVo();
        BigDecimal nunota = vo.asBigDecimalOrZero("NUNOTA");
        DynamicVO cabVO = cabDAO.findByPK(nunota);
        String tipmov = cabVO.asString("TIPMOV");
        if("V".equals(tipmov) ) {
            atualizaNota(nunota);
            //Utilitarios.recalculaImpostosNota(nunota);

        }
    }

    @Override
    public void afterDelete(PersistenceEvent persistenceEvent) throws Exception {

    }

    @Override
    public void beforeCommit(TransactionContext transactionContext) throws Exception {

    }
}
