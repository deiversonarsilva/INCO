package br.com.sankhya.bhz.acoes;

import br.com.sankhya.bhz.utils.AcessoBanco;
import br.com.sankhya.bhz.utils.ErroUtils;
import br.com.sankhya.extensions.actionbutton.AcaoRotinaJava;
import br.com.sankhya.extensions.actionbutton.ContextoAcao;
import br.com.sankhya.jape.dao.JdbcWrapper;
import br.com.sankhya.modelcore.util.EntityFacadeFactory;

import java.math.BigDecimal;
import java.sql.ResultSet;

public class acaoTipoAmostra implements AcaoRotinaJava {
    @Override
    public void doAction(ContextoAcao contextoAcao) throws Exception {
        JdbcWrapper jdbc = EntityFacadeFactory.getDWFFacade().getJdbcWrapper();
        jdbc.openSession();
        AcessoBanco acessoBanco = new AcessoBanco();
        try{
            acessoBanco.openSession();
            ResultSet mp = acessoBanco.find("SELECT CODPROD FROM TGFPRO WHERE USOPROD='M'");
            ResultSet it = acessoBanco.find("SELECT CODPROD FROM TGFPRO WHERE USOPROD='2'");
            ResultSet eb = acessoBanco.find("SELECT CODPROD FROM TGFPRO WHERE USOPROD='E'");
            while(mp.next()){
                BigDecimal codprod = mp.getBigDecimal("CODPROD");
                BigDecimal count = acessoBanco.findOne("SELECT COUNT(*) AS COUNT FROM TGFAMP WHERE CODPROD=? AND CODTIPAMOSTRA=?",codprod,new BigDecimal(7)).getBigDecimal("COUNT");
                if(count.compareTo(BigDecimal.ZERO)==0){
                    acessoBanco.update("INSERT INTO TGFAMP(CODPROD,CODTIPAMOSTRA,QTDPORAMOSTRA,FORMULA) VALUES(?,?,?,?)", codprod, new BigDecimal(7), new BigDecimal(0.1), BigDecimal.ONE);
                }


            }

            while(it.next()){
                BigDecimal codprod = it.getBigDecimal("CODPROD");
                BigDecimal count = acessoBanco.findOne("SELECT COUNT(*) AS COUNT FROM TGFAMP WHERE CODPROD=? AND CODTIPAMOSTRA=?",codprod,new BigDecimal(1)).getBigDecimal("COUNT");
                if(count.compareTo(BigDecimal.ZERO)==0){

                    acessoBanco.update("INSERT INTO TGFAMP(CODPROD,CODTIPAMOSTRA,QTDPORAMOSTRA,FORMULA) VALUES(?,?,?,?)", codprod, new BigDecimal(1), new BigDecimal(0.1), BigDecimal.ONE);
                }
            }
            while(eb.next()){
                BigDecimal codprod = eb.getBigDecimal("CODPROD");
                BigDecimal count = acessoBanco.findOne("SELECT COUNT(*) AS COUNT FROM TGFAMP WHERE CODPROD=? AND CODTIPAMOSTRA=?",codprod,new BigDecimal(9)).getBigDecimal("COUNT");
                if(count.compareTo(BigDecimal.ZERO)==0){

                    acessoBanco.update("INSERT INTO TGFAMP(CODPROD,CODTIPAMOSTRA,QTDPORAMOSTRA,FORMULA) VALUES(?,?,?,?)", codprod, new BigDecimal(9), new BigDecimal(0.1), BigDecimal.ONE);
                }
            }
        }finally {
            acessoBanco.closeSession();
        }
        jdbc.closeSession();
        contextoAcao.setMensagemRetorno("Processo Finalizado!!!");
    }
}
