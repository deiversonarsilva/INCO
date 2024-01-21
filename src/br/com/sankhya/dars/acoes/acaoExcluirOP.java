package br.com.sankhya.dars.acoes;

import br.com.sankhya.extensions.actionbutton.AcaoRotinaJava;
import br.com.sankhya.extensions.actionbutton.ContextoAcao;
import br.com.sankhya.extensions.actionbutton.Registro;
import br.com.sankhya.jape.dao.JdbcWrapper;
import br.com.sankhya.jape.vo.DynamicVO;
import br.com.sankhya.jape.wrapper.JapeFactory;
import br.com.sankhya.jape.wrapper.JapeWrapper;
import br.com.sankhya.jape.wrapper.fluid.FluidUpdateVO;
import br.com.sankhya.modelcore.util.EntityFacadeFactory;

import java.math.BigDecimal;
import java.util.Collection;

public class acaoExcluirOP implements AcaoRotinaJava {
    @Override
    public void doAction(ContextoAcao contextoAcao) throws Exception {
        JdbcWrapper jdbc = EntityFacadeFactory.getDWFFacade().getJdbcWrapper();
        jdbc.openSession();
        JapeWrapper idiDAO = JapeFactory.dao("CabecalhoInstanciaProcesso");//TPRIPROC
        JapeWrapper ipaDAO = JapeFactory.dao("ProdutoAcabadoAProduzir");//TPRIPA
        JapeWrapper iatvDAO = JapeFactory.dao("InstanciaAtividade");//TPRIATV
        JapeWrapper eiatvDAO = JapeFactory.dao("ExecucaoAtividade");//TPREIATV
        JapeWrapper apoDAO = JapeFactory.dao("CabecalhoApontamento");//TPRAPO
        JapeWrapper apaDAO = JapeFactory.dao("ApontamentoPA");//TPRAPA
        JapeWrapper idepDAO = JapeFactory.dao("DependenciaEntreOps");//TPRIDEP
        JapeWrapper esrDAO = JapeFactory.dao("EstoqueRepositorioPA");//TPRESR
        JapeWrapper merDAO = JapeFactory.dao("MovimentacaoRepositorioPA");//TPRMER
        JapeWrapper ramDAO = JapeFactory.dao("RegistroAmostras");//TGFRAM
        JapeWrapper iccDAO = JapeFactory.dao("ItemCicloControleQualidade");//TPRICCQ
        BigDecimal idiproc = BigDecimal.ZERO;
        BigDecimal idiatv = BigDecimal.ZERO;
        BigDecimal nuapo = BigDecimal.ZERO;
        Registro[] linhas = contextoAcao.getLinhas();
        for (Registro linha : linhas) {
            idiproc = (BigDecimal) linha.getCampo("IDIPROC");
            Collection<DynamicVO> idiatvVO = iatvDAO.find("IDIPROC=?",idiproc);
            merDAO.deleteByCriteria("IDIPROC=?",idiproc);
            esrDAO.deleteByCriteria("IDIPROC=?",idiproc);
            idepDAO.deleteByCriteria("IDIPROCPI=? ",idiproc);
            idepDAO.deleteByCriteria("IDIPROCPA=? ",idiproc);
            ipaDAO.deleteByCriteria("IDIPROC=?",idiproc);
            for(DynamicVO iatvVO : idiatvVO){
                idiatv = iatvVO.asBigDecimalOrZero("IDIATV");
                Collection<DynamicVO> apo = apoDAO.find("IDIATV=?",idiatv);
                for(DynamicVO apoVO : apo) {
                    nuapo = apoVO.asBigDecimal("NUAPO");
                    apaDAO.deleteByCriteria("NUAPO=?",nuapo);
                    apoDAO.deleteByCriteria("NUAPO=?",nuapo);
                }
                eiatvDAO.deleteByCriteria("IDIATV=?",idiatv);
            }
            iatvDAO.deleteByCriteria("IDIPROC=?",idiproc);
            ramDAO.deleteByCriteria("IDIPROC=?",idiproc);
            iccDAO.deleteByCriteria("IDIPROC=?",idiproc);
            idiDAO.deleteByCriteria("IDIPROC=?",idiproc);
        }
        jdbc.closeSession();
        contextoAcao.setMensagemRetorno("Processo Finalizado!!!");
    }
}
