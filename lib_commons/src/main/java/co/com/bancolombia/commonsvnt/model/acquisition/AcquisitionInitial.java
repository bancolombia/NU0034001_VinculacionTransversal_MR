package co.com.bancolombia.commonsvnt.model.acquisition;

import co.com.bancolombia.commonsvnt.api.model.util.MyDataInitial;
import lombok.Builder;

@Builder
public class AcquisitionInitial {
	private Object meta;
	private MyDataInitial data;

    public AcquisitionInitial(Object meta, MyDataInitial data) {
        this.meta = meta;
        this.data = data;
    }

	public Object getMeta() {
		return meta;
	}

	public void setMeta(Object meta) {
		this.meta = meta;
	}

	public MyDataInitial getData() {
		return data;
	}

	public void setData(MyDataInitial data) {
		this.data = data;
	}
    
    

	
}
