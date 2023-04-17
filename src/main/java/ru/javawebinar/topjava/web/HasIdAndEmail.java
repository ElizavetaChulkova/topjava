package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.HasId;

public interface HasIdAndEmail extends HasId {
    String getEmail();
}
