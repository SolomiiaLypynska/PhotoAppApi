package com.appdevelopervlog.photoapp.api.users.data;

import com.appdevelopervlog.photoapp.api.users.service.UsersServiceImpl;
import com.appdevelopervlog.photoapp.api.users.ui.model.AlbumResponseModel;
import feign.FeignException;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@FeignClient(name = "albums-ws", fallbackFactory = AlbusmsFallbackFactory.class)
public interface AlbumsServiceClient {

    @GetMapping("/users/{id}/albums")
    List<AlbumResponseModel> getAlbums(@PathVariable String id);
}

@Component
class AlbusmsFallbackFactory implements FallbackFactory<AlbumsServiceClient> {


    @Override
    public AlbumsServiceClient create(Throwable throwable) {
        return new AlbumsServiceClientFallback(throwable);
    }
}

class AlbumsServiceClientFallback implements AlbumsServiceClient {
    Logger logger = LoggerFactory.getLogger(AlbumsServiceClientFallback.class);

    private final Throwable throwable;

    public AlbumsServiceClientFallback(Throwable throwable) {
        this.throwable = throwable;
    }

    @Override
    public List<AlbumResponseModel> getAlbums(String id) {
        if (throwable instanceof FeignException && ((FeignException) throwable).status() == 404){
            logger.error("404 Error when userId {} . Error message - {}", id, throwable.getLocalizedMessage() );
        } else {
            logger.error("Other error took place: {}", throwable.getLocalizedMessage());
        }
        return new ArrayList<>();
    }
}
