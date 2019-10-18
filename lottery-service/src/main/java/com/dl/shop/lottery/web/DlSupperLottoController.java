package com.dl.shop.lottery.web;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.lottery.dto.DlSuperLottoRewardDTO;
import com.dl.lottery.param.SupperLottoParam;
import com.dl.shop.lottery.model.DlSuperLottoReward;
import com.dl.shop.lottery.service.DlSuperLottoService;

import io.swagger.annotations.ApiOperation;
 
@RestController
@RequestMapping("/lottery/supperLotto")
public class DlSupperLottoController {
	private final static Logger logger = LoggerFactory.getLogger(DlSupperLottoController.class);
    @Resource
    private DlSuperLottoService   dlSuperLottoService;
    
	@ApiOperation(value = "中奖详情", notes = "中奖详情")
	@PostMapping("/byTermNum")
	public BaseResult< List<DlSuperLottoRewardDTO>> byTermNum(@RequestBody SupperLottoParam param) {
			List<DlSuperLottoReward> superLottoRewardList = dlSuperLottoService.findByTermNum(param.getTermNum());
			List<DlSuperLottoRewardDTO> superLottoRewardListDTO = new ArrayList<DlSuperLottoRewardDTO>();
			for (int i = 0; i < superLottoRewardList.size(); i++) {
				DlSuperLottoRewardDTO superLottoRewardDTO =new DlSuperLottoRewardDTO();
				try {
					BeanUtils.copyProperties(superLottoRewardDTO, superLottoRewardList.get(i));
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
				superLottoRewardListDTO.add(superLottoRewardDTO);
			}
			return ResultGenerator.genSuccessResult(null, superLottoRewardListDTO);
		}
}
