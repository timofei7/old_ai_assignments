#!/usr/bin/perl -w

while(<>) {
    s/[^a-zA-Z\d\s]//g;
	    print $_;
		}

