package com.sise.subappstore.modelo;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * Holder para el Adapter del Spinner
 * @author danielme.com
 *
 */
public class CategoriaNetworkHolder
{

	private ImageView icono;

	private TextView textView;

	public ImageView getIcono()
	{
		return icono;
	}

	public void setIcono(ImageView icono) 
	{
		this.icono = icono;
	}

	public TextView getTextView() 
	{
		return textView;
	}

	public void setTextView(TextView textView) 
	{
		this.textView = textView;
	}

}
