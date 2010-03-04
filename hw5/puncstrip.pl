#!/usr/bin/perl -w

while(<>) {
    s/[^a-zA-Z\s]//g;
	    print $_;
		}

