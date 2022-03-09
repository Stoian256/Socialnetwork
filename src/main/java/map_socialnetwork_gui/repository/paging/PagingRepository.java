package com.lab6.map_socialnetwork_gui.repository.paging;

import com.lab6.map_socialnetwork_gui.repository.IRepository;

public interface PagingRepository<T> extends IRepository<T> {

    Page<T> findAll(Pageable pageable);   // Pageable e un fel de paginator
}
