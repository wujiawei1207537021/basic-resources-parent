package com.wu.basic.dynamic.controller;

import com.wu.framework.easy.excel.util.FastExcelImp;
import com.wu.framework.easy.stereotype.upsert.dynamic.QuickEasyUpsert;
import com.wu.framework.easy.stereotype.upsert.entity.EasyHashMap;
import com.wu.framework.easy.stereotype.upsert.enums.EasyUpsertType;
import com.wu.framework.easy.stereotype.web.EasyController;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

/**
 * description 基础数据字典
 *
 * @author 吴佳伟
 * @date 2021/1/21 下午7:59
 */
@EasyController
public class BasicDataDictionaryController {

    /**
     * description 中文翻译成英文
     *
     * @param
     * @return
     * @exception/throws
     * @author 吴佳伟
     * @date 2021/1/21 下午8:03
     */
    @GetMapping("/chinese/english/{part}")
    public void chineseToEnglish(@PathVariable String part) {

    }

    /**
     * description 英文翻译成中文
     *
     * @param
     * @return
     * @exception/throws
     * @author 吴佳伟
     * @date 2021/1/21 下午8:04
     */
    @GetMapping("/english/chinese/{part}")
    public void englishToChinese(@PathVariable String part) {

    }

    /**
     * description 解析存储Excel文件
     *
     * @param
     * @return
     * @exception/throws
     * @author 吴佳伟
     * @date 2021/1/21 下午8:15
     */
    @QuickEasyUpsert(type = EasyUpsertType.MySQL)
    @PostMapping("/analyze/save")
    public List<EasyHashMap> storeExcelFiles(MultipartFile multipartFile) throws Exception {
        String contentType = multipartFile.getContentType();
        Reader reader = new InputStreamReader(multipartFile.getInputStream());
        List<EasyHashMap> easyHashMapList = FastExcelImp.parseExcel(multipartFile, EasyHashMap.class);

        if (ObjectUtils.isEmpty(easyHashMapList)) {
            return easyHashMapList;
        }
        EasyHashMap firstEasyHashMap = new EasyHashMap(multipartFile.getName());
        firstEasyHashMap.putAll(easyHashMapList.get(0));
        easyHashMapList.remove(0);
        easyHashMapList.add(0, firstEasyHashMap);
        return easyHashMapList;
    }
}
