package com.cloud.photo.trans;


import com.cloud.photo.trans.entity.StorageObject;
import com.cloud.photo.trans.mapper.StorageObjectMapper;
import com.cloud.photo.trans.service.IStorageObjectService;
import org.checkerframework.checker.units.qual.A;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class StorageObjectTest {
    @Autowired
    private IStorageObjectService iStorageObjectService;
    @Autowired
    private StorageObjectMapper storageObjectMapper;

    @Test
    public void test(){
        //final StorageObject storageObject = iStorageObjectService.getById("1679441554104471554");
        final StorageObject storageObject = storageObjectMapper.selectById("1679441554104471554");
        System.out.println("storageObject = " + storageObject);
    }


}
