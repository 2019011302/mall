package com.mall.service.Impl;

import com.mall.service.ImgService;
import com.mall.vo.ResStatus;
import com.mall.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Service
@Slf4j
public class ImgServiceImpl implements ImgService {

    @Override
    public ResultVo upload(MultipartFile file, String name, String path) {
        if(file.isEmpty()){
            return new ResultVo(ResStatus.NO , "fail" , null);
        }
        try{
            String fileName= file.getOriginalFilename();
            String str =  fileName.substring(fileName.lastIndexOf("."));
            File f = new File(path+name+str);
            file.transferTo(f);
        }catch (Exception e){
            e.printStackTrace();
            new ResultVo(ResStatus.NO , "fail" , null);
        }
        return new ResultVo(ResStatus.OK , "success" , null);
    }
}
