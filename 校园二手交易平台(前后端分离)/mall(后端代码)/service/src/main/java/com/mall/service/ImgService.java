package com.mall.service;

import com.mall.vo.ResultVo;
import org.springframework.web.multipart.MultipartFile;

public interface ImgService {
    public ResultVo upload(MultipartFile file, String name, String path);
}
