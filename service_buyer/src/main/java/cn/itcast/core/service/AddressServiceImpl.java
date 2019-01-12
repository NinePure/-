package cn.itcast.core.service;

import cn.itcast.core.dao.address.AddressDao;
import cn.itcast.core.pojo.address.Address;
import cn.itcast.core.pojo.address.AddressQuery;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressDao addressDao;

    @Override
    public List<Address> findAddressListByUserName(String userName) {
        AddressQuery query = new AddressQuery();
        AddressQuery.Criteria criteria = query.createCriteria();
        criteria.andUserIdEqualTo(userName);
        List<Address> addressList = addressDao.selectByExample(query);
        return addressList;
    }

    @Override
    public void saveAddress(Address address) {
        address.setIsDefault("0");
        addressDao.insertSelective(address);
    }

    @Override
    public void setDefault(String name, String id) {
        AddressQuery addressQuery = new AddressQuery();
        AddressQuery.Criteria criteria = addressQuery.createCriteria();
        criteria.andUserIdEqualTo(name);
        Address address = new Address();
        address.setIsDefault("0");
        addressDao.updateByExampleSelective(address, addressQuery);
        address.setIsDefault("1");
        address.setId(Long.parseLong(id));
        addressDao.updateByPrimaryKeySelective(address);
    }
}
