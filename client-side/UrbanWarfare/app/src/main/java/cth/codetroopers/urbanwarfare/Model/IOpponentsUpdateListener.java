package cth.codetroopers.urbanwarfare.Model;

import java.util.List;

/**
 * Created by lumo on 11/05/17.
 */

public interface IOpponentsUpdateListener {

    void updateOpponents(List<PlayerSkeleton> opponents);
}