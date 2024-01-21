package br.com.sankhya.dars.utils;

import br.com.sankhya.jape.util.JapeSessionContext;
import br.com.sankhya.modelcore.auth.AuthenticationInfo;
import br.com.sankhya.modelcore.comercial.BarramentoRegra;
import br.com.sankhya.modelcore.comercial.CentralFinanceiro;
import br.com.sankhya.modelcore.comercial.LiberacaoSolicitada;
import br.com.sankhya.modelcore.comercial.centrais.CACHelper;
import br.com.sankhya.modelcore.comercial.impostos.ImpostosHelpper;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class Utilitarios {


    public static BigDecimal ONE_HUNDRED = new BigDecimal(100);
    public static MathContext mathContext = new MathContext(4, RoundingMode.HALF_EVEN);



    public static void recalculaImpostosNota(BigDecimal nuNota) throws Exception {
        ImpostosHelpper impostohelp = new ImpostosHelpper();
        impostohelp.setForcarRecalculo(true);
        impostohelp.setSankhya(false);
        impostohelp.calcularImpostos(nuNota);
    }

    public static void confirmarNota(BigDecimal nuNota) throws Exception {
        String toResult="";
        CACHelper cacHelper = new CACHelper();

        BarramentoRegra barramento = BarramentoRegra.build(CACHelper.class,
                "regrasConfirmacaoCAC.xml", AuthenticationInfo.getCurrent());
        cacHelper.confirmarNota(nuNota, barramento, false);


        if (barramento.getLiberacoesSolicitadas().size() == 0 &&
                barramento.getErros().size() == 0) {
            System.out.println("Nota Confirmada " + nuNota + "");

        } else {
            if (barramento.getErros().size() > 0) {
                System.out.println("Erro na confirma��o " +
                        nuNota);

                for (Exception e : barramento.getErros()) {
                    toResult =
                            e.getMessage();
                    break;
                }
            }

            if (barramento.getLiberacoesSolicitadas().size() > 0) {
                System.out.println("Erro na confirma��o " + nuNota
                        + ". Foi solicitada libera��es");
                toResult = "Libera��es solicitadas - \n";
                for (LiberacaoSolicitada e :
                        barramento.getLiberacoesSolicitadas()) {
                    toResult += "Evento: "
                            + e.getEvento() + (e.getDescricao() != null ? " Descri��o:  "
                            + e.getDescricao() + "\n" : "\n");
                    break;
                }

            }

        }
        System.out.println(toResult);
    }
    public static void refazerFinanceiro(BigDecimal nuNota) throws Exception {
        //Intrinsics.checkParameterIsNotNull(nuNota, "nuNota");
        JapeSessionContext.putProperty("br.com.sankhya.com.CentralCompraVenda", Boolean.TRUE);
        JapeSessionContext.putProperty("br.com.sankhya.com.CentralCompraVenda", Boolean.TRUE);
        JapeSessionContext.putProperty("ItemNota.incluindo.alterando.pela.central", Boolean.TRUE);
        JapeSessionContext.putProperty("calcular.outros.impostos", "false");
        CentralFinanceiro financeiro = new CentralFinanceiro();
        financeiro.inicializaNota(nuNota);
        financeiro.refazerFinanceiro();
    }


}


