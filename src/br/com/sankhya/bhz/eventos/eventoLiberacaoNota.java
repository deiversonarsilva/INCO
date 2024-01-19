package br.com.sankhya.bhz.eventos;

import br.com.sankhya.bhz.utils.ErroUtils;
import br.com.sankhya.extensions.eventoprogramavel.EventoProgramavelJava;
import br.com.sankhya.jape.event.PersistenceEvent;
import br.com.sankhya.jape.event.TransactionContext;
import br.com.sankhya.jape.vo.DynamicVO;
import br.com.sankhya.jape.wrapper.JapeFactory;
import br.com.sankhya.jape.wrapper.JapeWrapper;
import com.sankhya.util.TimeUtils;

import java.math.BigDecimal;
import java.util.Collection;

public class eventoLiberacaoNota implements EventoProgramavelJava { //TSILIB

    public void deletaliberacao(BigDecimal nunota) throws Exception {
        JapeWrapper cabDAO = JapeFactory.dao("CabecalhoNota");
        JapeWrapper libDAO = JapeFactory.dao("LiberacaoLimite");
        DynamicVO cabVO = cabDAO.findOne("NUNOTA=?",nunota);
        DynamicVO libVO = libDAO.findOne("NUCHAVE=?",nunota);
        if(libVO != null){
            String tipmov = cabVO.asString("TIPMOV");
            if("V".equals(tipmov) || "D".equals(tipmov)) {
                Collection<DynamicVO> vo = libDAO.find("NUCHAVE=?", nunota);
                for (DynamicVO lib : vo) {
                    libDAO.prepareToUpdate(lib)
                            .set("CODUSULIB", BigDecimal.ZERO)
                            .set("DHLIB", TimeUtils.getNow())
                            .set("VLRLIBERADO", lib.asBigDecimalOrZero("VLRATUAL"))
                            .update();
                }
            }
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
        BigDecimal nunota = vo.asBigDecimal("NUCHAVE");
        deletaliberacao(nunota);
    }

    @Override
    public void afterUpdate(PersistenceEvent persistenceEvent) throws Exception {
        DynamicVO vo = (DynamicVO) persistenceEvent.getVo();
        BigDecimal nunota = vo.asBigDecimal("NUCHAVE");
        deletaliberacao(nunota);
    }

    @Override
    public void afterDelete(PersistenceEvent persistenceEvent) throws Exception {

    }

    @Override
    public void beforeCommit(TransactionContext transactionContext) throws Exception {

    }
}
