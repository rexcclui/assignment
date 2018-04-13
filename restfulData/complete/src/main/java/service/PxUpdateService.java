package service;

import java.io.IOException;

public class PxUpdateService {


    public PxUpdateService(double px) {
        if (px>0)
            try {
                PxSvr.update(px);
            } catch (IOException e) {
                status = "FAIL";
            }
    }

    public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	private String status = "OK";
    
}
