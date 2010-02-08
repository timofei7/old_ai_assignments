'''
Created on Feb 2, 2010

@author: tim
for cs44 w10
'''
import sys, random

class Computer(object):
    '''
    the computer player
    '''
    maxdepth = 100
    maxstates = 10000

    def __init__(self, s):
        """Constructor"""
        self.state = s  #the initial state
        self.statecounter = 0 #states examined
        self.depthcount = 0
        
    def cutoff_test(self, s, depth):
        """ returns true if we should stop"""        
#        1. We have reached a terminal state (a win or a draw) 
#        2. We have reached the specified maximum depth. 
#        3. We have visited the specified maximum number of states.
        (b, p) = s.is_win()
        return b or s.legal_moves() == "" or self.depthcount >= depth or self.statecounter >= self.maxstates
            
        
    def minimaxi(self, s, who):
        """i like to make computer decisions"""
        self.statecounter = 0;  #reset the counter
        movevalues = {}
        depth = 0
        finding = True
        while (finding):  # iterative deepening
            self.depthcount = 0 #reset the depthcounter
            for move in s._legal_moves():
                self.statecounter = self.statecounter+1
                s.do_move(move)
                if who == "X": #if i go first i'm maxi
                    movevalues[self.my_min(s,depth, move)] = move
                else: #if i go second i'm mini
                    movevalues[self.my_max(s,depth, move)] = move
                s.undo_move(move)
            if depth >= self.maxdepth:
                finding = False  #stop and return result if we've found a win... TODO: correct?
            else:
                depth = depth +1
        print "mnnx    state count: " + str(self.statecounter) + " depth: " + str(depth)
        if who == "X":
            print "    eval move: " + str(max(movevalues.keys()))
            return movevalues[max(movevalues.keys())]
        else:
            print "    eval move: " + str(min(movevalues.keys()))
            return movevalues[min(movevalues.keys())]
        
        
    def my_min(self, s,depth, orig):
        """i like to minimize"""
        if self.cutoff_test(s, depth):
            return self.utility(s)
        v = sys.maxint - 1
        self.depthcount = self.depthcount + 1
        for move in s._legal_moves():
            self.statecounter = self.statecounter+1
            s.do_move(move) #do the move
            v = min(v, self.my_max(s,depth, orig)) #recurse
            s.undo_move(move) #undo the move when we return from the above
        self.depthcount = self.depthcount - 1
        return v


    def my_max(self, s,depth, orig):
        """i like to maximize!"""
        if self.cutoff_test(s, depth):
            return self.utility(s)
        v = -sys.maxint - 1
        self.depthcount = self.depthcount + 1
        for move in s._legal_moves():
            self.statecounter = self.statecounter+1
            s.do_move(move) #do the move
            v = max(v, self.my_min(s, depth, orig)) #recurse
            s.undo_move(move) #undo the move when we return from the above
        self.depthcount = self.depthcount - 1
        return v
        
        
    def utility(self, s):
        """the utility function"""
        val = 0
        
        if s.legal_moves() == "":  #end of game
            val = 0
        else:
            val = self.evaluate(s.build_segments())
            #return random.randint(-(sys.maxint/2), (sys.maxint/2))
        return val
            
            
    def evaluate(self, segs):
        """the evaluation function for minimax"""
        # I decided to evaluate each board state separately rather than keeping track
        # this may be expensive as I have to do a bit of data shuffling around
        # when I create the segments. but heck its just a constant right?
        
        def e(seg, pos):
            """0 if s contains no disks or disks from both players,
               1 if s contains one X,
               10 if s contains two X's
               100 if s contains three X's"""
               
            
            if seg.count(pos) == 1 and seg.count(".") == 3:
                return 1
            elif seg.count(pos) == 2 and seg.count(".") == 2:
                return 10
            elif seg.count(pos) == 3 and seg.count(".") == 1:
                return 100
            elif seg.count(pos) == 4:  #win
                return sys.maxint -1
            elif seg.count(".") == 4:  #empty
                return 0
            else:  #any other mixed group
                return 0
    
        val = 0
        for seg in segs:
            val = val + e(seg, "X")
            val = val - e(seg, "O")
        
        return val    
        
        