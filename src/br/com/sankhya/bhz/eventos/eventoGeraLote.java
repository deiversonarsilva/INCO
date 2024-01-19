package br.com.sankhya.bhz.eventos;

import br.com.sankhya.bhz.utils.AcessoBanco;
import br.com.sankhya.extensions.eventoprogramavel.EventoProgramavelJava;
import br.com.sankhya.jape.event.PersistenceEvent;
import br.com.sankhya.jape.event.TransactionContext;
import br.com.sankhya.jape.vo.DynamicVO;
import br.com.sankhya.jape.wrapper.JapeFactory;
import br.com.sankhya.jape.wrapper.JapeWrapper;
import com.sankhya.util.BigDecimalUtil;
import com.sankhya.util.StringUtils;
import com.sankhya.util.TimeUtils;
import sun.awt.image.BufferedImageGraphicsConfig;

import java.math.BigDecimal;

import java.sql.Timestamp;
import java.util.Collection;

public class eventoGeraLote implements EventoProgramavelJava {//TGFITE

    public void Geralote(BigDecimal nunota, BigDecimal sequencia) throws Exception {
        JapeWrapper iteDAO = JapeFactory.dao("ItemNota");
        JapeWrapper cabDAO = JapeFactory.dao("CabecalhoNota");
        JapeWrapper numDAO = JapeFactory.dao("ControleNumeracao");
        JapeWrapper topDAO = JapeFactory.dao("TipoOperacao");
        DynamicVO cabVO = cabDAO.findOne("NUNOTA=?",nunota);
        DynamicVO iteVO = iteDAO.findOne("NUNOTA=? AND SEQUENCIA=?",nunota,sequencia);
        DynamicVO topVO = topDAO.findOne("CODTIPOPER=? AND DHALTER=?",cabVO.asBigDecimal("CODTIPOPER"), cabVO.asTimestamp("DHTIPOPER"));
        BigDecimal codemp = cabVO.asBigDecimal("CODEMP");
        String tipmov = cabVO.asString("TIPMOV");

        BigDecimal ano = BigDecimal.ZERO;

        if(!"C".equals(tipmov) ){
            return;
        }

        AcessoBanco acessoBanco = new AcessoBanco();
                try{
                    acessoBanco.openSession();
                    ano = acessoBanco.findOne("SELECT SNK_FORMAT_DATE(SYSDATE,'YYYY') AS ANO FROM DUAL").getBigDecimal("ANO");
                    DynamicVO numVO = numDAO.findOne("NOMEARQ='Nro Lote MP Autom.' AND ARQUIVO LIKE '%'||(SELECT SNK_FORMAT_DATE(SYSDATE,'YYYY') AS ANO FROM DUAL) AND  CODEMP=?",codemp);


                    BigDecimal ultcod = numVO.asBigDecimal("ULTCOD");
                    iteDAO.prepareToUpdate(iteVO)
                            .set("CONTROLE",ultcod.add(BigDecimal.ONE)+"/"+ano)
                            .update();
                    numDAO.prepareToUpdate(numVO)
                            .set("ULTCOD",ultcod.add(BigDecimal.ONE))
                            .update();
                }finally {
                    acessoBanco.closeSession();
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
        BigDecimal nunota = vo.asBigDecimal("NUNOTA");
        BigDecimal sequencia = vo.asBigDecimal("SEQUENCIA");
        Geralote(nunota,sequencia);
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
