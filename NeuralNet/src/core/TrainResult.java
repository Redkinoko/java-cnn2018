/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

/**
 *
 * @author Red
 */
public class TrainResult
{
    public float  percent;
    public String comment;

    TrainResult(float percent, String comment)
    {
        this.percent = percent;
        this.comment = comment;
    }
}
